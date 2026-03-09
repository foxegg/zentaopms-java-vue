package com.zentao.service;

import com.zentao.entity.Bug;
import com.zentao.entity.Product;
import com.zentao.entity.Project;
import com.zentao.entity.Release;
import com.zentao.entity.Story;
import com.zentao.entity.Task;
import com.zentao.repository.ProductRepository;
import com.zentao.repository.ReleaseRepository;
import com.zentao.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 导出服务 - Excel 等
 */
@Service
@RequiredArgsConstructor
public class ExportService {

    private final BugService bugService;
    private final TaskService taskService;
    private final com.zentao.service.StoryService storyService;
    private final ReleaseRepository releaseRepository;
    private final ProductRepository productRepository;
    private final ProjectService projectService;

    public byte[] exportBugs(Specification<Bug> spec, int maxRows) throws IOException {
        Page<Bug> page = bugService.getList(spec, Pageable.ofSize(Math.min(maxRows, 10000)));
        List<Bug> bugs = page.getContent();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Bug");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            String[] headers = {"ID", "产品", "项目", "标题", "严重程度", "状态", "指派给", "创建者", "创建日期"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Bug b : bugs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(b.getId() != null ? b.getId() : 0);
                row.createCell(1).setCellValue(b.getProduct() != null ? b.getProduct() : 0);
                row.createCell(2).setCellValue(b.getProject() != null ? b.getProject() : 0);
                row.createCell(3).setCellValue(b.getTitle() != null ? b.getTitle() : "");
                row.createCell(4).setCellValue(b.getSeverity() != null ? b.getSeverity() : 0);
                row.createCell(5).setCellValue(b.getStatus() != null ? b.getStatus() : "");
                row.createCell(6).setCellValue(b.getAssignedTo() != null ? b.getAssignedTo() : "");
                row.createCell(7).setCellValue(b.getOpenedBy() != null ? b.getOpenedBy() : "");
                row.createCell(8).setCellValue(b.getOpenedDate() != null ? b.getOpenedDate().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportTasks(Specification<Task> spec, int maxRows) throws IOException {
        Page<Task> page = taskService.getList(spec, Pageable.ofSize(Math.min(maxRows, 10000)));
        List<Task> tasks = page.getContent();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Task");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            String[] headers = {"ID", "项目", "执行", "名称", "状态", "指派给", "预计", "消耗", "创建者", "创建日期"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Task t : tasks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(t.getId() != null ? t.getId() : 0);
                row.createCell(1).setCellValue(t.getProject() != null ? t.getProject() : 0);
                row.createCell(2).setCellValue(t.getExecution() != null ? t.getExecution() : 0);
                row.createCell(3).setCellValue(t.getName() != null ? t.getName() : "");
                row.createCell(4).setCellValue(t.getStatus() != null ? t.getStatus() : "");
                row.createCell(5).setCellValue(t.getAssignedTo() != null ? t.getAssignedTo() : "");
                row.createCell(6).setCellValue(t.getEstimate() != null ? t.getEstimate().doubleValue() : 0);
                row.createCell(7).setCellValue(t.getConsumed() != null ? t.getConsumed().doubleValue() : 0);
                row.createCell(8).setCellValue(t.getOpenedBy() != null ? t.getOpenedBy() : "");
                row.createCell(9).setCellValue(t.getOpenedDate() != null ? t.getOpenedDate().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportStories(Specification<Story> spec, int maxRows) throws IOException {
        Page<Story> page = storyService.getList(spec != null ? spec : (r, q, cb) -> null, Pageable.ofSize(Math.min(maxRows, 10000)));
        List<Story> stories = page.getContent();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Story");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            String[] headers = {"ID", "产品", "标题", "类型", "优先级", "状态", "指派给", "预计", "创建者", "创建日期"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Story s : stories) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId() != null ? s.getId() : 0);
                row.createCell(1).setCellValue(s.getProduct() != null ? s.getProduct() : 0);
                row.createCell(2).setCellValue(s.getTitle() != null ? s.getTitle() : "");
                row.createCell(3).setCellValue(s.getType() != null ? s.getType() : "");
                row.createCell(4).setCellValue(s.getPri() != null ? s.getPri() : 0);
                row.createCell(5).setCellValue(s.getStatus() != null ? s.getStatus() : "");
                row.createCell(6).setCellValue(s.getAssignedTo() != null ? s.getAssignedTo() : "");
                row.createCell(7).setCellValue(s.getEstimate() != null ? s.getEstimate().doubleValue() : 0);
                row.createCell(8).setCellValue(s.getOpenedBy() != null ? s.getOpenedBy() : "");
                row.createCell(9).setCellValue(s.getOpenedDate() != null ? s.getOpenedDate().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportReleases(Integer productId, int maxRows) throws IOException {
        List<Release> full = productId != null && productId > 0
                ? releaseRepository.findByProductAndDeletedOrderByDateDesc(productId, 0)
                : releaseRepository.findAll().stream().filter(r -> r.getDeleted() != null && r.getDeleted() == 0).toList();
        List<Release> list = full.size() <= maxRows ? full : full.subList(0, maxRows);

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Release");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            String[] headers = {"ID", "产品", "名称", "日期", "状态", "创建日期"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }
            int rowNum = 1;
            for (Release r : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getId() != null ? r.getId() : 0);
                row.createCell(1).setCellValue(r.getProduct() != null ? r.getProduct() : 0);
                row.createCell(2).setCellValue(r.getName() != null ? r.getName() : "");
                row.createCell(3).setCellValue(r.getDate() != null ? r.getDate().toString() : "");
                row.createCell(4).setCellValue(r.getStatus() != null ? r.getStatus() : "");
                row.createCell(5).setCellValue(r.getCreatedDate() != null ? r.getCreatedDate().toString() : "");
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportProducts(int maxRows) throws IOException {
        List<Product> list = productRepository.findByDeletedOrderByOrderNumAsc(0).stream().limit(maxRows).toList();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Product");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            String[] headers = {"ID", "名称", "代码", "类型", "状态", "PO"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }
            int rowNum = 1;
            for (Product p : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId() != null ? p.getId() : 0);
                row.createCell(1).setCellValue(p.getName() != null ? p.getName() : "");
                row.createCell(2).setCellValue(p.getCode() != null ? p.getCode() : "");
                row.createCell(3).setCellValue(p.getType() != null ? p.getType() : "");
                row.createCell(4).setCellValue(p.getStatus() != null ? p.getStatus() : "");
                row.createCell(5).setCellValue(p.getPo() != null ? p.getPo() : "");
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportProjects(String status, int maxRows) throws IOException {
        Specification<Project> spec = (root, q, cb) ->
            (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status))
                ? cb.equal(root.get("status"), status)
                : cb.conjunction();
        Page<Project> page = projectService.getList(spec, Pageable.ofSize(Math.min(maxRows, 10000)));
        List<Project> list = page.getContent();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Project");
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            String[] headers = {"ID", "名称", "代码", "类型", "状态", "开始日期", "结束日期", "负责人"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }
            int rowNum = 1;
            for (Project p : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId() != null ? p.getId() : 0);
                row.createCell(1).setCellValue(p.getName() != null ? p.getName() : "");
                row.createCell(2).setCellValue(p.getCode() != null ? p.getCode() : "");
                row.createCell(3).setCellValue(p.getModel() != null ? p.getModel() : "");
                row.createCell(4).setCellValue(p.getStatus() != null ? p.getStatus() : "");
                row.createCell(5).setCellValue(p.getBegin() != null ? p.getBegin().toString() : "");
                row.createCell(6).setCellValue(p.getEnd() != null ? p.getEnd().toString() : "");
                row.createCell(7).setCellValue(p.getOpenedBy() != null ? p.getOpenedBy() : "");
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }
}
