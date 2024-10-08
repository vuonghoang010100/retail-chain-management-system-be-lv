package com.example.sales_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Report", description = "The Report API.")
public class ReportController {

    @PersistenceContext(unitName = "tenant")
    EntityManager entityManager;

    @GetMapping("/1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report1(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        if (storeId == -7) {
            String sql =
                    "SELECT " +
                    "DATE(o.create_time) AS NgayBanHang, " +
                    "COUNT(o.id) AS TongSoDonHang, " +
                    "SUM(od.sub_total) AS TongDoanhThu, " +
                    "SUM(od.sub_total - b.purchase_price * od.quantity) AS LoiNhuan " +
                    "FROM orders o " +
                    "JOIN order_detail od ON o.id = od.order_id " +
                    "JOIN batch b ON od.batch_id = b.id " +
                    "WHERE DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                    "GROUP BY DATE(o.create_time) " +
                    "ORDER BY DATE(o.create_time)";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, from).setParameter(2, to);
            return query.getResultList();
        }
        String sql =
                        "SELECT " +
                        "DATE(o.create_time) AS NgayBanHang, " +
                        "COUNT(o.id) AS TongSoDonHang, " +
                        "SUM(od.sub_total) AS TongDoanhThu, " +
                        "SUM(od.sub_total - b.purchase_price * od.quantity) AS LoiNhuan " +
                        "FROM orders o " +
                        "JOIN order_detail od ON o.id = od.order_id " +
                        "JOIN batch b ON od.batch_id = b.id " +
                        "WHERE o.store_id = ?1 " +
                        "AND DATE(o.create_time) BETWEEN ?2 AND ?3 " +
                        "GROUP BY DATE(o.create_time) " +
                        "ORDER BY DATE(o.create_time)";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, storeId)
                .setParameter(2, from)
                .setParameter(3, to);
        return query.getResultList();

    }

    @GetMapping("/2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report2(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        if (storeId == -7) {
            String sql =
                    "SELECT " +
                            "p.id as MaSanPham, " +
                            "p.name AS TenSanPham, " +
                            "COALESCE(SUM(od.quantity), 0) AS TongSoLuongBan, " +
                            "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu, " +
                            "COALESCE(SUM(od.sub_total - b.purchase_price * od.quantity), 0) AS LoiNhuan " +
                            "FROM product p " +
                            "LEFT JOIN order_detail od ON p.id = od.product_id " +
                            "LEFT JOIN orders o ON od.order_id = o.id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                            "LEFT JOIN batch b ON od.batch_id = b.id " +
                            "GROUP BY p.id, p.name " +
                            "ORDER BY p.id, p.name";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, from).setParameter(2, to);
            return query.getResultList();
        }
        String sql =
                "SELECT " +
                        "p.id as MaSanPham, " +
                        "p.name AS TenSanPham, " +
                        "COALESCE(SUM(od.quantity), 0) AS TongSoLuongBan, " +
                        "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu, " +
                        "COALESCE(SUM(od.sub_total - b.purchase_price * od.quantity), 0) AS LoiNhuan " +
                        "FROM product p " +
                        "LEFT JOIN order_detail od ON p.id = od.product_id AND od.store_id = ?1 " +
                        "LEFT JOIN orders o ON od.order_id = o.id AND DATE(o.create_time) BETWEEN ?2 AND ?3 " +
                        "LEFT JOIN batch b ON od.batch_id = b.id " +
                        "GROUP BY p.id, p.name " +
                        "ORDER BY p.id, p.name";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, storeId).setParameter(2, from).setParameter(3, to);
        return query.getResultList();
    }


    @GetMapping("/3")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report3(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        if (storeId == -7) {
            String sql =
                    "SELECT " +
                            "c.name AS TenNhomSanPham, " +
                            "COALESCE(SUM(od.quantity), 0) AS TongSoLuongBan, " +
                            "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu, " +
                            "COALESCE(SUM(od.sub_total - b.purchase_price * od.quantity), 0) AS LoiNhuan " +
                            "FROM category c " +
                            "LEFT JOIN product p ON c.id = p.category_id " +
                            "LEFT JOIN order_detail od ON p.id = od.product_id " +
                            "LEFT JOIN orders o ON od.order_id = o.id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                            "LEFT JOIN batch b ON od.batch_id = b.id " +
                            "GROUP BY c.name " +
                            "ORDER BY c.name";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, from).setParameter(2, to);
            return query.getResultList();
        }
        String sql =
                "SELECT " +
                        "c.name AS TenNhomSanPham, " +
                        "COALESCE(SUM(od.quantity), 0) AS TongSoLuongBan, " +
                        "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu, " +
                        "COALESCE(SUM(od.sub_total - b.purchase_price * od.quantity), 0) AS LoiNhuan " +
                        "FROM category c " +
                        "LEFT JOIN product p ON c.id = p.category_id " +
                        "LEFT JOIN order_detail od ON p.id = od.product_id AND od.store_id = ?3 " +
                        "LEFT JOIN orders o ON od.order_id = o.id AND DATE(o.create_time) BETWEEN ?1 AND ?2  " +
                        "LEFT JOIN batch b ON od.batch_id = b.id " +
                        "GROUP BY c.name " +
                        "ORDER BY c.name";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to).setParameter(3, storeId);
        return query.getResultList();
    }

    @GetMapping("/4")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report4(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        if (storeId == -7) {
            String sql =
                    "SELECT " +
                            "e.id as MaNhanVien," +
                            "e.full_name AS TenNhanVien, " +
                            "COUNT(o.id) AS TongSoDonHang, " +
                            "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu, " +
                            "COALESCE(SUM(od.sub_total - b.purchase_price * od.quantity), 0) AS LoiNhuan " +
                            "FROM employee e " +
                            "LEFT JOIN orders o ON e.id = o.employee_id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                            "LEFT JOIN order_detail od ON o.id = od.order_id " +
                            "LEFT JOIN batch b ON od.batch_id = b.id " +
                            "GROUP BY e.id, e.full_name " +
                            "ORDER BY e.id, e.full_name";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, from).setParameter(2, to);
            return query.getResultList();
        }
        String sql =
                "SELECT " +
                        "e.id as MaNhanVien," +
                        "e.full_name AS TenNhanVien, " +
                        "COUNT(o.id) AS TongSoDonHang, " +
                        "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu, " +
                        "COALESCE(SUM(od.sub_total - b.purchase_price * od.quantity), 0) AS LoiNhuan " +
                        "FROM employee e " +
                        "LEFT JOIN orders o ON e.id = o.employee_id AND DATE(o.create_time) BETWEEN ?1 AND ?2 AND o.store_id = ?3 " +
                        "LEFT JOIN order_detail od ON o.id = od.order_id  " +
                        "LEFT JOIN batch b ON od.batch_id = b.id " +
                        "GROUP BY e.id, e.full_name " +
                        "ORDER BY e.id, e.full_name";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to).setParameter(3, storeId);
        return query.getResultList();
    }


    @GetMapping("/5")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report5(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        String sql =
                "SELECT " +
                        "s.id AS MaCuaHang," +
                        "s.name AS TenCuaHang, " +
                        "COUNT(o.id) AS TongSoDonHang, " +
                        "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu, " +
                        "COALESCE(SUM(od.sub_total - b.purchase_price * od.quantity), 0) AS LoiNhuan " +
                        "FROM store s " +
                        "LEFT JOIN orders o ON s.id = o.store_id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                        "LEFT JOIN order_detail od ON o.id = od.order_id " +
                        "LEFT JOIN batch b ON od.batch_id = b.id " +
                        "GROUP BY s.id, s.name " +
                        "ORDER BY s.id, s.name";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getResultList();
    }


    @GetMapping("/6")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report6(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        if (storeId == -7) {
            String sql =
                    "SELECT " +
                            "c.id AS customer_id, " +
                            "c.full_name AS customer_name, " +
                            "COUNT(o.id) AS total_orders, " +
                            "COALESCE(SUM(od.sub_total), 0) AS total_revenue, " +
                            "COALESCE(SUM(od.sub_total - b.purchase_price * COALESCE(od.quantity, 0) - COALESCE(o.discount, 0)), 0) AS total_profit " +
                            "FROM customer c " +
                            "LEFT JOIN orders o ON c.id = o.customer_id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                            "LEFT JOIN order_detail od ON o.id = od.order_id " +
                            "LEFT JOIN batch b ON od.batch_id = b.id " +
                            "GROUP BY c.id, c.full_name " +
                            "ORDER BY total_revenue DESC";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, from).setParameter(2, to);
            return query.getResultList();
        }
        String sql =
                "SELECT " +
                        "c.id AS customer_id, " +
                        "c.full_name AS customer_name, " +
                        "COUNT(o.id) AS total_orders, " +
                        "COALESCE(SUM(od.sub_total), 0) AS total_revenue, " +
                        "COALESCE(SUM(od.sub_total - b.purchase_price * COALESCE(od.quantity, 0) - COALESCE(o.discount, 0)), 0) AS total_profit " +
                        "FROM customer c " +
                        "LEFT JOIN orders o ON c.id = o.customer_id AND o.store_id = ?1 AND DATE(o.create_time) BETWEEN ?2 AND ?3 " +
                        "LEFT JOIN order_detail od ON o.id = od.order_id " +
                        "LEFT JOIN batch b ON od.batch_id = b.id " +
                        "GROUP BY c.id, c.full_name " +
                        "ORDER BY total_revenue DESC";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, storeId).setParameter(2, from).setParameter(3, to);
        return query.getResultList();
    }

    @GetMapping("/7")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report7(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        if (storeId == -7) {
            String sql =
                    "SELECT " +
                            "DATE(p.create_time) AS NgayNhapHang, " +
                            "COUNT(p.id) AS TongSoDonNhap, " +
                            "SUM(pd.sub_total) AS TongGiaTriNhap " +
                            "FROM purchase p " +
                            "JOIN purchase_detail pd ON p.id = pd.purchase_id " +
                            "WHERE DATE(p.create_time) BETWEEN ?1 AND ?2 " +
                            "GROUP BY DATE(p.create_time) " +
                            "ORDER BY DATE(p.create_time)";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, from).setParameter(2, to);
            return query.getResultList();
        }
        String sql =
                "SELECT " +
                        "DATE(p.create_time) AS NgayNhapHang, " +
                        "COUNT(p.id) AS TongSoDonNhap, " +
                        "SUM(pd.sub_total) AS TongGiaTriNhap " +
                        "FROM purchase p " +
                        "JOIN purchase_detail pd ON p.id = pd.purchase_id " +
                        "WHERE DATE(p.create_time) BETWEEN ?1 AND ?2 AND p.store_id = ?3 " +
                        "GROUP BY DATE(p.create_time) " +
                        "ORDER BY DATE(p.create_time)";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to).setParameter(3, storeId);
        return query.getResultList();
    }

    @GetMapping("/8")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report8(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        String sql =
                "SELECT " +
                        "s.id, " +
                        "s.name AS TenCuaHang, " +
                        "COUNT(p.id) AS TongSoDonNhap, " +
                        "COALESCE(SUM(pd.sub_total), 0) AS TongGiaTriNhap " +
                        "FROM store s " +
                        "LEFT JOIN purchase p ON s.id = p.store_id AND DATE(p.create_time) BETWEEN ?1 AND ?2 " +
                        "LEFT JOIN purchase_detail pd ON p.id = pd.purchase_id " +
                        "GROUP BY s.id, s.name " +
                        "ORDER BY s.id, s.name";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getResultList();
    }

    @GetMapping("/9")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report9(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        if (storeId == -7) {
            String sql =
                    "SELECT " +
                            "v.id as MaNhaCungCap, " +
                            "v.full_name AS TenNhaCungCap, " +
                            "COUNT(p.id) AS TongSoDonNhap, " +
                            "COALESCE(SUM(pd.sub_total), 0) AS TongGiaTriNhap " +
                            "FROM vendor v " +
                            "LEFT JOIN purchase p ON v.id = p.vendor_id AND DATE(p.create_time) BETWEEN ?1 AND ?2 " +
                            "LEFT JOIN purchase_detail pd ON p.id = pd.purchase_id " +
                            "GROUP BY v.id, v.full_name " +
                            "ORDER BY v.id, v.full_name";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, from).setParameter(2, to);
            return query.getResultList();
        }
        String sql =
                "SELECT " +
                        "v.id as MaNhaCungCap, " +
                        "v.full_name AS TenNhaCungCap, " +
                        "COUNT(p.id) AS TongSoDonNhap, " +
                        "COALESCE(SUM(pd.sub_total), 0) AS TongGiaTriNhap " +
                        "FROM vendor v " +
                        "LEFT JOIN purchase p ON v.id = p.vendor_id AND DATE(p.create_time) BETWEEN ?1 AND ?2 AND p.store_id = ?3 " +
                        "LEFT JOIN purchase_detail pd ON p.id = pd.purchase_id " +
                        "GROUP BY v.id, v.full_name " +
                        "ORDER BY v.id, v.full_name";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to).setParameter(3, storeId);
        return query.getResultList();
    }

    // 10


    @GetMapping("/11")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report11(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        String sql =
                "WITH RevenuePerProduct AS ( " +
                        "SELECT p.id AS ProductID, p.name AS TenSanPham, COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu " +
                        "FROM product p " +
                        "LEFT JOIN order_detail od ON p.id = od.product_id " +
                        "LEFT JOIN orders o ON od.order_id = o.id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                        "GROUP BY p.id, p.name), " +
                        "RankedRevenue AS ( " +
                        "SELECT ProductID, TenSanPham, TongDoanhThu, " +
                        "RANK() OVER (ORDER BY TongDoanhThu DESC) AS Rank, " +
                        "SUM(TongDoanhThu) OVER () AS TotalRevenue " +
                        "FROM RevenuePerProduct), " +
                        "CumulativeRevenue AS ( " +
                        "SELECT ProductID, TenSanPham, TongDoanhThu, Rank, TotalRevenue, " +
                        "SUM(TongDoanhThu) OVER (ORDER BY Rank) AS CumulativeRevenue, " +
                        "(SUM(TongDoanhThu) OVER (ORDER BY Rank) / TotalRevenue) * 100 AS CumulativePercentage " +
                        "FROM RankedRevenue) " +
                        "SELECT ProductID, TenSanPham, TongDoanhThu, " +
                        "(TongDoanhThu / TotalRevenue) * 100 AS RevenueRatio, " +
                        "CumulativePercentage, " +
                        "CASE " +
                        "WHEN CumulativePercentage <= 70 THEN 'A' " +
                        "WHEN CumulativePercentage <= 90 THEN 'B' " +
                        "ELSE 'C' " +
                        "END AS ABC_Category " +
                        "FROM CumulativeRevenue " +
                        "ORDER BY Rank";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getResultList();
    }


    @GetMapping("/12")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('REPORT')")
    public List<Object[]> report12(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        String sql =
                "WITH RevenuePerProduct AS ( " +
                        "SELECT p.id AS ProductID, p.name AS TenSanPham, COALESCE(SUM(od.quantity), 0) AS TongDoanhThu " +
                        "FROM product p " +
                        "LEFT JOIN order_detail od ON p.id = od.product_id " +
                        "LEFT JOIN orders o ON od.order_id = o.id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                        "GROUP BY p.id, p.name), " +
                        "RankedRevenue AS ( " +
                        "SELECT ProductID, TenSanPham, TongDoanhThu, " +
                        "RANK() OVER (ORDER BY TongDoanhThu DESC) AS Rank, " +
                        "SUM(TongDoanhThu) OVER () AS TotalRevenue " +
                        "FROM RevenuePerProduct), " +
                        "CumulativeRevenue AS ( " +
                        "SELECT ProductID, TenSanPham, TongDoanhThu, Rank, TotalRevenue, " +
                        "SUM(TongDoanhThu) OVER (ORDER BY Rank) AS CumulativeRevenue, " +
                        "(SUM(TongDoanhThu) OVER (ORDER BY Rank) / TotalRevenue) * 100 AS CumulativePercentage " +
                        "FROM RankedRevenue) " +
                        "SELECT ProductID, TenSanPham, TongDoanhThu, " +
                        "(TongDoanhThu / TotalRevenue) * 100 AS RevenueRatio, " +
                        "CumulativePercentage, " +
                        "CASE " +
                        "WHEN CumulativePercentage <= 75 THEN 'Fast-moving' " +
                        "WHEN CumulativePercentage <= 95 THEN 'Slow-moving' " +
                        "ELSE 'Non-moving' " +
                        "END AS ABC_Category " +
                        "FROM CumulativeRevenue " +
                        "ORDER BY Rank";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getResultList();
    }


}
