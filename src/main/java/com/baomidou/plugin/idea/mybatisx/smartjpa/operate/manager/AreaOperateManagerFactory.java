package com.baomidou.plugin.idea.mybatisx.smartjpa.operate.manager;

import com.baomidou.plugin.idea.mybatisx.smartjpa.component.TxField;
import com.baomidou.plugin.idea.mybatisx.smartjpa.operate.dialect.mysql.MysqlManager;
import com.baomidou.plugin.idea.mybatisx.smartjpa.operate.dialect.oracle.OracleManager;
import com.intellij.database.Dbms;
import com.intellij.database.model.DasTable;
import com.intellij.psi.PsiClass;

import java.util.List;

public class AreaOperateManagerFactory {

    public static AreaOperateManager getByDbms(Dbms dbms, List<TxField> mappingField, PsiClass entityClass, DasTable dasTable, String tableName) {
        if (dbms == Dbms.ORACLE) {
            return new OracleManager(mappingField, entityClass,dasTable,tableName);
        }
        return new MysqlManager(mappingField, entityClass);
    }
}
