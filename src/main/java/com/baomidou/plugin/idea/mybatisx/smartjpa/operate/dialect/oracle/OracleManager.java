package com.baomidou.plugin.idea.mybatisx.smartjpa.operate.dialect.oracle;

import com.baomidou.plugin.idea.mybatisx.smartjpa.component.TxField;
import com.baomidou.plugin.idea.mybatisx.smartjpa.operate.dialect.BaseDialectManager;
import com.baomidou.plugin.idea.mybatisx.smartjpa.operate.DeleteOperator;
import com.baomidou.plugin.idea.mybatisx.smartjpa.operate.InsertOperator;
import com.baomidou.plugin.idea.mybatisx.smartjpa.operate.SelectOperator;
import com.baomidou.plugin.idea.mybatisx.smartjpa.operate.UpdateOperator;
import com.intellij.database.model.DasTable;
import com.intellij.psi.PsiClass;

import java.util.List;

/**
 * oracle 方言
 */
public class OracleManager extends BaseDialectManager {
    private DasTable dasTable;
    private String tableName;

    public OracleManager(List<TxField> mappingField, PsiClass entityClass, DasTable dasTable, String tableName) {
        this.dasTable = dasTable;
        this.tableName = tableName;
        init(mappingField, entityClass);
    }

    @Override
    protected void init(List<TxField> mappingField, PsiClass entityClass) {
        registerManagers(new SelectOperator(mappingField, entityClass));
        this.registerManagers(new InsertOperator(mappingField) {
            @Override
            protected void initCustomArea(String areaName, List<TxField> mappingField) {
                super.initCustomArea(areaName, mappingField);
                // insert into 的方式批量插入
                OracleInsertBatchWithUnion oracleInsertBatch = new OracleInsertBatchWithUnion(dasTable, tableName);
                oracleInsertBatch.initInsertBatch(areaName, mappingField);
                this.registerStatementBlock(oracleInsertBatch.getStatementBlock());
                this.addOperatorName(oracleInsertBatch.operatorName());

                // insert all into table 的方式批量插入
                OracleInsertBatchWithAll oracleInsertBatchWithAll = new OracleInsertBatchWithAll(dasTable, tableName);
                oracleInsertBatchWithAll.initInsertBatch(areaName, mappingField);
                this.registerStatementBlock(oracleInsertBatchWithAll.getStatementBlock());
                this.addOperatorName(oracleInsertBatchWithAll.operatorName());
            }
        });
        registerManagers(new UpdateOperator(mappingField));
        registerManagers(new DeleteOperator(mappingField));
    }
}
