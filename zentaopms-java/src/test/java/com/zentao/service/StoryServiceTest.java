package com.zentao.service;

import com.zentao.entity.Story;
import com.zentao.repository.RelationRepository;
import com.zentao.repository.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * 与 PHP-JAVA-SERVICE-COMPARISON 报告对应的 StoryService 行为测试（需求关联、getPairs 等）。
 */
@ExtendWith(MockitoExtension.class)
class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;
    @Mock
    private RelationRepository relationRepository;
    @Mock
    private ActionService actionService;

    @InjectMocks
    private StoryService storyService;

    private Story story1;
    private Story story2;

    @BeforeEach
    void setUp() {
        story1 = new Story();
        story1.setId(1);
        story1.setProduct(10);
        story1.setType("story");
        story1.setVersion(1);
        story1.setLinkStories("");
        story1.setDeleted(0);

        story2 = new Story();
        story2.setId(2);
        story2.setProduct(10);
        story2.setType("story");
        story2.setVersion(1);
        story2.setLinkStories("");
        story2.setDeleted(0);
    }

    @Test
    @DisplayName("linkStory: 双向维护 linkStories，并写入 zt_relation linkedto/linkedfrom")
    void linkStory_bidirectionalAndRelation() {
        when(storyRepository.findById(1)).thenReturn(Optional.of(story1));
        when(storyRepository.findById(2)).thenReturn(Optional.of(story2));
        when(storyRepository.save(any(Story.class))).thenAnswer(i -> i.getArgument(0));

        storyService.linkStory(1, 2);

        assertThat(story1.getLinkStories()).contains("2");
        assertThat(story2.getLinkStories()).contains("1");
        verify(storyRepository, times(2)).save(any(Story.class));
        verify(relationRepository, times(2)).save(any());
        verify(actionService, times(2)).create(eq("story"), anyInt(), eq("linkrelatedstory"), eq(""), any());
    }

    @Test
    @DisplayName("unlinkStory: 双向移除 linkStories，并删除 zt_relation、记两条 unlinkrelatedstory")
    void unlinkStory_bidirectionalAndDeleteRelation() {
        story1.setLinkStories("2");
        story2.setLinkStories("1");
        when(storyRepository.findById(1)).thenReturn(Optional.of(story1));
        when(storyRepository.findById(2)).thenReturn(Optional.of(story2));
        when(storyRepository.save(any(Story.class))).thenAnswer(i -> i.getArgument(0));

        storyService.unlinkStory(1, 2);

        assertThat(story1.getLinkStories()).doesNotContain("2");
        assertThat(story2.getLinkStories()).doesNotContain("1");
        verify(storyRepository, times(2)).save(any(Story.class));
        verify(relationRepository).deleteStoryLink(1, 2);
        verify(actionService).create("story", 1, "unlinkrelatedstory", "", "2");
        verify(actionService).create("story", 2, "unlinkrelatedstory", "", "1");
    }

    @Test
    @DisplayName("getPairs(productId, 0, true) 等价于仅按 product，无 plan 过滤")
    void getPairs_noPlanFilter() {
        when(storyRepository.findByProductAndDeleted(10, 0)).thenReturn(java.util.List.of(story1, story2));
        story1.setTitle("A");
        story2.setTitle("B");

        var pairs = storyService.getPairs(10, 0, true);

        assertThat(pairs).containsEntry(1, "A").containsEntry(2, "B");
    }

    @Test
    @DisplayName("getPairsByList 空列表返回空 Map")
    void getPairsByList_empty() {
        var pairs = storyService.getPairsByList(java.util.List.of());
        assertThat(pairs).isEmpty();
    }

    @Test
    @DisplayName("getPairsByList(ids, true) 包含 0->空串")
    void getPairsByList_withEmptyOption() {
        story1.setTitle("T1");
        when(storyRepository.findAllById(java.util.List.of(1))).thenReturn(java.util.List.of(story1));

        var pairs = storyService.getPairsByList(java.util.List.of(1), true);

        assertThat(pairs).containsKey(0);
        assertThat(pairs.get(0)).isEmpty();
        assertThat(pairs).containsEntry(1, "T1");
    }

    @Test
    @DisplayName("linkStory 需求不存在时抛异常")
    void linkStory_storyNotFound() {
        when(storyRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storyService.linkStory(1, 2))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("需求不存在");
    }
}
