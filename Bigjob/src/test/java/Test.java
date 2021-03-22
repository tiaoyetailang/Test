public class Test {
    public static void main(String[] args) {
        new String("select * from (" +
                "select '业务凭证' type,m.voucherno,\n" +
                "       (select g.ofvoucherno from hxuser.gfvoucher g where g.voucherno=m.voucherno and rownum<2) ofvoucherno,\n" +
                "       m.voucherdate,\n" +
                "       m.payeecode,\n" +
                "       m.payee,\n" +
                "       m.paydate,\n" +
                "       decode(t.payway,'109','现金支付', '110', '现金支付','111','现金支付', '210', '银行转账','211','支票支付', '213', '汇票支付', '411','转入暂收款', '412','转入暂收款',  '413', '转入暂收款', '418','转入暂收款',\n" +
                "        '419','转入暂收款',  '420','转入暂收款',  '511','冲暂收款',  '512', '冲暂收款', '513','冲暂收款',  '516', '冲暂收款', '518', '冲暂收款', '519', '冲暂收款', '520','冲暂收款','其他' ) payway,\n" +
                "       t.checkno,\n" +
                "       t.settlementcurrency,\n" +
                "       t.settlementfee,\n" +
                "       t.ofbankaccountcode,\n" +
                "       (select g.bankaccountcode\n" +
                "          from hxuser.gfbankaccount g\n" +
                "         where g.ofbankaccountcode = t.ofbankaccountcode\n" +
                "           and g.currency = t.settlementcurrency) bankaccountcode,\n" +
                "       (select g.accountename\n" +
                "          from hxuser.gfbankaccount g\n" +
                "         where g.ofbankaccountcode = t.ofbankaccountcode\n" +
                "           and g.currency = t.settlementcurrency) accountename,\n" +
                "        m.paymentno\n" +
                "  from hxuser.gppaymentdetail t, hxuser.gppaymentmain m\n" +
                " where t.paymentno = m.paymentno\n" +
                "   and t.chargetimes = m.chargetimes\n" +
                "   and to_char(m.paydate, 'yyyymm') between '202004' and '202004'\n" +
                "   and m.combinepaymentno is null\n" +
                "   and m.recstatus='1'\n" +
                "   and (t.payway like '1%' or t.payway like '2%')\n" +
                "   /*and (t.payway in ('109', '110', '111', '210', '211', '213', '411', '412', '413', '418',\n" +
                "        '419', '420', '511', '512', '513', '516', '518', '519', '520')*/\n" +
                "union all\n" +
                "select '财务凭证' type,voucherno,voucherno,voucherdate,'' payeecode,remark payee,voucherdate paydate,''  payway, checkno,currency,(t.debitsource - t.creditsource) settlementfee,t.itemcode||replace(t.directionidx,'/','') ofbankaccountcode,\n" +
                "       (select g.bankaccountcode\n" +
                "          from hxuser.gfbankaccount g\n" +
                "         where g.ofbankaccountcode = (t.itemcode||replace(t.directionidx,'/',''))\n" +
                "           and g.currency = t.currency) bankaccountcode,\n" +
                "       (select g.accountename\n" +
                "          from hxuser.gfbankaccount g\n" +
                "         where g.ofbankaccountcode = (t.itemcode||replace(t.directionidx,'/',''))\n" +
                "           and g.currency = t.currency) accountename,\n" +
                "          '' paymentno\n" +
                " from cwuser.accsubvoucher t,cwuser.accmonthtrace g where t.yearmonth=g.yearmonth and g.accmonthstat not in('5','7') and t.yearmonth between '202004' and '202004' and voucherno like 'B%' and itemcode in('1001','1002')\n" +
                "union all\n" +
                "select '财务凭证' type,voucherno,voucherno,voucherdate,'' payeecode,remark payee,voucherdate paydate,''  payway, checkno,currency,(t.debitsource - t.creditsource) settlementfee,t.itemcode||replace(t.directionidx,'/','') ofbankaccountcode,\n" +
                "       (select g.bankaccountcode\n" +
                "          from hxuser.gfbankaccount g\n" +
                "         where g.ofbankaccountcode = (t.itemcode||replace(t.directionidx,'/',''))\n" +
                "           and g.currency = t.currency) bankaccountcode,\n" +
                "       (select g.accountename\n" +
                "          from hxuser.gfbankaccount g\n" +
                "         where g.ofbankaccountcode = (t.itemcode||replace(t.directionidx,'/',''))\n" +
                "           and g.currency = t.currency) accountename,\n" +
                "          '' paymentno\n" +
                " from cwuser.accsubvoucherhis t,cwuser.accmonthtrace g where t.yearmonth=g.yearmonth and g.accmonthstat  in('5','7') and t.yearmonth between '202004' and '202004' and voucherno like 'B%' and itemcode in('1001','1002')\n" +
                " ) order by type,voucherdate,voucherno");
    }
}
