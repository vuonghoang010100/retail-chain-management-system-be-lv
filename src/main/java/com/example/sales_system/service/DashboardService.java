package com.example.sales_system.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DashboardService {
    @PersistenceContext(unitName = "tenant")
    EntityManager entityManager;

    public Object getTotalRevenue(LocalDate from, LocalDate to) {
        String sql = "SELECT SUM(o.total) AS total_revenue " +
                "FROM orders o " +
                "WHERE DATE(o.create_time) BETWEEN ?1 AND ?2 ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getSingleResult();
    }

    public Object getTotalProfit(LocalDate from, LocalDate to) {
        String sql = "SELECT SUM(od.sub_total - b.purchase_price * COALESCE(od.quantity, 0) - COALESCE(o.discount, 0)) AS total_profit " +
                "FROM orders o " +
                "JOIN order_detail od ON o.id = od.order_id " +
                "JOIN batch b ON od.batch_id = b.id " +
                "WHERE DATE(o.create_time) BETWEEN ?1 AND ?2 ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getSingleResult();
    }

    public Object getTotalOrders(LocalDate from, LocalDate to) {
        String sql = "SELECT " +
                "COUNT(*) AS total_orders " +
                "FROM orders " +
                "WHERE DATE(create_time) BETWEEN ?1 AND ?2 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getSingleResult();
    }

    public Object getAvgOrderValue(LocalDate from, LocalDate to) {
        String sql =
                "SELECT " +
                "SUM(od.sub_total) / COUNT(o.id) " +
                "FROM orders o " +
                "JOIN order_detail od ON o.id = od.order_id " +
                "WHERE DATE(o.create_time) BETWEEN ?1 AND ?2 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getSingleResult();
    }

    public Object getAvgBasket(LocalDate from, LocalDate to) {
        String sql =
                "SELECT " +
                "    SUM(od.quantity) / COUNT(o.id) " +
                "FROM  orders o " +
                "JOIN order_detail od ON o.id = od.order_id " +
                "WHERE DATE(o.create_time) BETWEEN ?1 AND ?2 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getSingleResult();
    }

    public Object getTotalSellProduct(LocalDate from, LocalDate to) {
        String sql =
                "SELECT " +
                        "SUM(od.quantity) " +
                        "FROM  order_detail od " +
                        "JOIN orders o ON od.order_id = o.id " +
                        "WHERE DATE(o.create_time) BETWEEN ?1 AND ?2 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getSingleResult();
    }

    public Object getNewCustomer(LocalDate from, LocalDate to) {
        String sql =
                "SELECT " +
                        "COUNT(DISTINCT c.id) AS new_customers " +
                        "FROM  customer c " +
                        "WHERE DATE(c.create_time) BETWEEN ?1 AND ?2 ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getSingleResult();
    }

    public List<Object[]> getTopStores(LocalDate from, LocalDate to) {
        String sql = "SELECT " +
                "s.id AS id, " +
                "s.name AS TenCuaHang, " +
                "COUNT(o.id) AS TongSoDonHang, " +
                "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu " +
                "FROM store s " +
                "LEFT JOIN orders o ON s.id = o.store_id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                "LEFT JOIN order_detail od ON o.id = od.order_id " +
                "LEFT JOIN batch b ON od.batch_id = b.id " +
                "GROUP BY s.id, s.name " +
                "ORDER BY TongDoanhThu DESC " +
                "LIMIT 5";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getResultList();
    }

    public List<Object[]> getTopEmployees(LocalDate from, LocalDate to) {
        String sql = "SELECT " +
                "e.id as MaNhanVien, " +
                "e.full_name AS TenNhanVien, " +
                "COUNT(o.id) AS TongSoDonHang, " +
                "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu " +
                "FROM employee e " +
                "LEFT JOIN orders o ON e.id = o.employee_id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                "LEFT JOIN order_detail od ON o.id = od.order_id " +
                "LEFT JOIN batch b ON od.batch_id = b.id " +
                "GROUP BY e.id, e.full_name " +
                "ORDER BY TongDoanhThu DESC " +
                "LIMIT 5";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getResultList();
    }

    public List<Object[]> getTopProducts(LocalDate from, LocalDate to) {
        String sql = "SELECT " +
                "p.id as MaSanPham, " +
                "p.name AS TenSanPham, " +
                "p.image_url as image_url, " +
                "COALESCE(SUM(od.quantity), 0) AS TongSoLuongBan, " +
                "COALESCE(SUM(od.sub_total), 0) AS TongDoanhThu " +
                "FROM product p " +
                "LEFT JOIN order_detail od ON p.id = od.product_id and od.store_id = 1 " +
                "LEFT JOIN orders o ON od.order_id = o.id AND DATE(o.create_time) BETWEEN ?1 AND ?2 " +
                "LEFT JOIN batch b ON od.batch_id = b.id " +
                "GROUP BY p.id, p.name " +
                "ORDER BY TongSoLuongBan DESC " +
                "LIMIT 10";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, from).setParameter(2, to);
        return query.getResultList();
    }

}
