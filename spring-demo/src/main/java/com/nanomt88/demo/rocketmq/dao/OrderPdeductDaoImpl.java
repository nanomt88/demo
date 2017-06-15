package com.nanomt88.demo.rocketmq.dao;


import com.lxft.nova.commons.criteria.pdeduct.RealtimePdeductListQueryCriteria;
import com.lxft.nova.commons.criteria.pdeduct.ReturnFileOrderListCriteria;
import com.lxft.nova.commons.type.pdeduct.OrderPdeductCheckStatus;
import com.lxft.nova.commons.type.pdeduct.OrderPdeductStatus;
import com.nanomt88.demo.rocketmq.entity.OrderPdeduct;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Created by whl on 3/3/17.
 */
@Repository(value = "orderPdeductDaoCustom")
public class OrderPdeductDaoImpl implements OrderPdeductDaoCustom {

    @PersistenceContext(unitName = "default")
    EntityManager em;

    private static final String TIME_PARSE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String LIST_SQL = " select op.* from t_order_pdeduct op where 1 = 1 ";

    private static final String COUNT_SQL = " select count(*) from t_order_pdeduct op where 1 = 1 ";

    @Override
    public List<OrderPdeduct> getOrderById(Long id) {

        StringBuilder hql = new StringBuilder();

        //根据卡前置、开后置类型判断是否需要连表
        hql.append(LIST_SQL);



        hql.append(" and id="+id);

        Query query = em.createNativeQuery(hql.toString(), OrderPdeduct.class);


        return query.getResultList();
    }

    @Override
    public Long count(RealtimePdeductListQueryCriteria criteria) {

        StringBuilder hql = new StringBuilder();

        //根据卡前置、开后置类型判断是否需要连表
        hql.append(COUNT_SQL);

        Map<String, Object> params = new HashMap<String, Object>();
        injectParams(criteria, params, hql);

        Query query = em.createNativeQuery(hql.toString());

        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }

        return Long.valueOf(query.getSingleResult().toString());
    }

    /**
     * SELECT * FROM t_order_status_flow where previous_status in(2,5) and now_status IN(0,4,6)  AND previous_accounting_date IN(20170101,20161231) AND accounting_date=20170102
     * @param criteria
     * @return
     */
    @Override
    public List<OrderPdeduct> returnFileOrderlist(ReturnFileOrderListCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT merchant_order_no,order_id,create_time,amount,fee,amount_settle,status,extra1,extra2,extra3 FROM t_order_pdeduct WHERE order_id IN(SELECT order_id FROM t_order_status_flow where previous_status IN(")
                .append(OrderPdeductStatus.PROCESSING.ordinal()).append(",")
                .append(OrderPdeductStatus.WAIT_REFUND.ordinal()).append(")").append(" AND now_status IN(")
                .append(OrderPdeductStatus.FAILED.ordinal()).append(",")
                .append(OrderPdeductStatus.PROCESSING_SETTLEMENT.ordinal()).append(",")
                .append(OrderPdeductStatus.PROCESSING_FAILED.ordinal()).append(",")
                .append(OrderPdeductStatus.PROCESSING_REFUND.ordinal()).append(")")
                .append("  AND previous_accounting_date IN(").append(criteria.getAccountingDate()).append(",")
                .append(criteria.getPreviousAccountingDate()).append(") AND accounting_date=")
                .append(criteria.getNowAccountingDate()).append(") AND merchant_id='").append(criteria.getMerchantId())
                .append("' ORDER BY create_time ASC");
        Query query = em.createNativeQuery(sql.toString());

        List rows = query.getResultList();
        if (rows.isEmpty()) {
            return new ArrayList<>();
        }
        return getOrderPdeductFromResult(rows);
    }

    /**
     * SELECT merchant_order_no,order_id,create_time,amount,fee,amount_settle,status,extra1,extra2,extra3 FROM t_order_pdeduct WHERE order_id IN(SELECT o.order_id FROM t_order_status_flow  o,t_order_pdeduct_detail d  where o.order_id=d.order_no AND o.previous_status =
     * 2 AND o.now_status=3 AND o.previous_accounting_date =20170302 AND o.accounting_date=20170302  AND d.check_status=6) AND merchant_id='XXXXXX'
     * @param criteria
     * @return
     */
    @Override
    public List<OrderPdeduct> billFileOrderlist(ReturnFileOrderListCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT merchant_order_no,order_id,create_time,amount,fee,amount_settle,status,extra1,extra2,extra3 FROM t_order_pdeduct WHERE order_id IN(SELECT o.order_id FROM t_order_status_flow  o,t_order_pdeduct_detail d  WHERE o.order_id=d.order_no AND o.previous_status =")
                .append(OrderPdeductStatus.PROCESSING.ordinal()).append(" AND o.now_status=")
                .append(OrderPdeductStatus.SUCCEED.ordinal())
                .append("  AND o.previous_accounting_date =").append(criteria.getAccountingDate()).append(" AND o.accounting_date=")
                .append(criteria.getAccountingDate()).append(" AND d.check_status=").append(OrderPdeductCheckStatus.FLAT.ordinal()).append(") AND merchant_id='").append(criteria.getMerchantId())
                .append("' ORDER BY create_time ASC");
        Query query = em.createNativeQuery(sql.toString());

        List rows = query.getResultList();
        if (rows.isEmpty()) {
            return new ArrayList<>();
        }

        return getOrderPdeductFromResult(rows);
    }



    /**
     * 转结算订单查询
     *SELECT merchant_order_no,order_id,create_time,amount,fee,amount_settle,status,extra1,extra2,extra3 FROM t_order_pdeduct WHERE order_id IN(SELECT order_id FROM t_order_status_flow where previous_status =2 AND now_status =4  AND accounting_date =20170324 UNION SELECT o.order_id FROM t_order_status_flow  o,t_order_pdeduct_detail d  where o.order_id=d.order_no AND o.previous_status =2 AND o.now_status=3  AND o.previous_accounting_date =20170324 AND o.accounting_date=20170324 AND d.check_status=6 ) AND merchant_id='888010044580002'  ORDER BY create_time ASC
     * @param criteria
     * @return
     */
    @Override
    public List<OrderPdeduct> settleOrderlist(ReturnFileOrderListCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT merchant_order_no,order_id,create_time,amount,fee,amount_settle,status,extra1,extra2,extra3 FROM t_order_pdeduct WHERE order_id IN(SELECT order_id FROM t_order_status_flow where previous_status =")
                .append(OrderPdeductStatus.PROCESSING.ordinal()).append(" AND now_status =")
                .append(OrderPdeductStatus.PROCESSING_SETTLEMENT.ordinal()).append(" AND accounting_date =").append(criteria.getAccountingDate())
                .append(" UNION SELECT o.order_id FROM t_order_status_flow  o,t_order_pdeduct_detail d  where o.order_id=d.order_no AND o.previous_status =")
                .append(OrderPdeductStatus.PROCESSING.ordinal()).append(" AND o.now_status=")
                .append(OrderPdeductStatus.SUCCEED.ordinal())
                .append("  AND o.previous_accounting_date =").append(criteria.getAccountingDate()).append(" AND o.accounting_date=")
                .append(criteria.getAccountingDate()).append(" AND d.check_status=").append(OrderPdeductCheckStatus.FLAT.ordinal()).append(" ) AND merchant_id='").append(criteria.getMerchantId()).append("'  ORDER BY create_time ASC");
        Query query = em.createNativeQuery(sql.toString());
        List rows = query.getResultList();
        if(rows.isEmpty()){
            return new ArrayList<>();
        }

        return getOrderPdeductFromResult(rows);
    }

    private List<OrderPdeduct> getOrderPdeductFromResult(  List rows ){
        List<OrderPdeduct> orders=new ArrayList<>();
        OrderPdeduct order;
        for (Object row:rows) {
            Object[] cells = (Object[]) row;
            order=new OrderPdeduct();
            order.setMerchantOrderNo(String.valueOf(cells[0]));
            order.setOrderId(String.valueOf(cells[1]));
            order.setCreateTime((Date) cells[2]);
            order.setAmount((BigDecimal) cells[3]);
            order.setFee((BigDecimal) cells[4]);
            order.setAmountSettle((BigDecimal) cells[5]);
            order.setStatus(OrderPdeductStatus.exits(Integer.valueOf(cells[6].toString())));
            order.setExtra1(String.valueOf(cells[7]));
            order.setExtra2(String.valueOf(cells[8]));
            order.setExtra3(String.valueOf(cells[9]));
            orders.add(order);
        }
        return orders;
    }
    private void injectParams(RealtimePdeductListQueryCriteria criteria, Map<String, Object> params,
                              StringBuilder hql) {

        if (StringUtils.isNotBlank(criteria.getMerchantOrderNo())) {
            hql.append(" and op.merchant_order_no = :merchantOrderNo ");
            params.put("merchantOrderNo", criteria.getMerchantOrderNo());
        }

        if (StringUtils.isNotBlank(criteria.getOrderId())) {
            hql.append(" and op.order_id = :orderId ");
            params.put("orderId", criteria.getOrderId());
        }

        if (StringUtils.isNotBlank(criteria.getMerchantId())) {
            hql.append(" and op.merchant_id = :merchantId ");
            params.put("merchantId", criteria.getMerchantId());
        }

        if (StringUtils.isNotBlank(criteria.getBankAccountNo())) {
            hql.append(" and op.bank_account_no = :bankAccountNo ");
            params.put("bankAccountNo", criteria.getBankAccountNo());
        }

        if (criteria.getOrderStatus() != null) {

            // 等待退款对外也是处理中
            if(StringUtils.equals(criteria.getOrderStatus().name(), OrderPdeductStatus.PROCESSING.name())){
                hql.append(" and ( op.status = :status1 or op.status = :status2)");
                params.put("status1", OrderPdeductStatus.PROCESSING.getValue());
                params.put("status2", OrderPdeductStatus.WAIT_REFUND.getValue());
            }else{
                hql.append(" and op.status = :status ");
                params.put("status", criteria.getOrderStatus().getValue());
            }
        }

    }

}
