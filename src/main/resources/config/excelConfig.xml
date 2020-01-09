<?xml version="1.0" encoding="utf-8"?>
<root>
    <!--    !!!!!!!!!!!!写在最前面: 查出来的数据一定一定要排序 ORDER BY!!!!!!!!!!!!!!!!!!!!!!!!-->
    <!--    !!!!!!!!!!!!写在最前面: 查出来的数据一定一定要排序 两层表头的排序order by firstFlex secondeFlex  firstHead!!!!!!!!!!!!!!!!!!!!!!!!-->
    <!--    配置模板-->
    <!--    表头支持颜色 -->
    <!--    数据部分 只支持满足条件的 字体变色 背景变色-->
    <!--    type  1表示固定表头（1层多层） 2 表示有动态表头（首层动态）3表示多层动态表头（首层次层都是动态）-->
    <!--    fillColor  表头背景颜色-->
    <!--    fontColor  表头字体颜色-->
    <!--    dataFontColor  数据字体颜色-->
    <!--    dataFillColor  数据字体颜色-->
    <!--    ifSymbol  条件符号 对于数值: >,>= ,< ,<=,= 对于文字: =  != (注意xml中这些符号的转义)-->
    <!--    ifTarget  比较对象-->
    <!--    changeDataFontColor  满足条件后字体颜色-->
    <!--    changeDataFillColor  满足条件后背景颜色-->
    <!--    precision  小数精度 0.00 -->
    <!--    percent  百分比精度 0.00% -->
    <!--    formula  为每一列指定不同的公式 -->
    <!--    columnWidth  为每一列指定不同宽度 值为字符个数 根据实际效果自己调整大小 -->
    <F00000>                              <!-- F00000 为示例-->
        <filesName>文件名</filesName>      <!-- 文件名会自动加上日期 格式-->
        <tableName>表名</tableName>        <!-- 暂时未使用到-->
        <sheetName>sheet名称</sheetName>
        <type>2</type>                     <!-- 表头的类型  类型为1  <flexHead>为空   类型为2  <flexHead>不为空 -->
        <firstHead>date</firstHead>        <!-- 首列表头 -->
        <flexHead>student</flexHead>       <!-- 一级动态表头 无动态表头则不需要此配置 -->
        <flexHead2>student</flexHead2>     <!-- 二级动态表头 无动态表头则不需要此配置 -->
        <structure>                        <!-- 表结构 structure的子标签名可自定义 -->
            <date name="日期" fillColor='#FFFF00' fontColor='#00008B'>null</date>
            <other name="其他" fillColor='#483D8B' fontColor='#00008B'>null</other>
            <student name="姓名" fontColor='#00008B'>   <!-- "姓名" 不会出现在表中 直接展示的student的值 -->
                <Chinese name="语文" fontColor='#00008B' ifSymbol="&lt;" ifTarget="2" changeDataFontColor='#FFFF00' changeDataFillColor='#FF0000' dataFontColor='#FFB6C1'
                         dataFillColor='#FF0000'>null
                </Chinese>
                <English name="英语" ifSymbol="&lt;" ifTarget="2" changeDataFontColor='#FF0000' changeDataFillColor='#FFFF00'>null</English>
                <math name="数学" ifSymbol="&lt;" ifTarget="2" changeDataFontColor='#FF0000' changeDataFillColor='#FFFF00'>null</math>
            </student>
        </structure>
        <structureExt>    <!-- 表结构ext 固定属性  用于表格末尾增加平均值 总计 等    MixFormula 为自定义函数 混合函数-->
            <sum name="总计" fillColor='#FFB6C1' dataFillColor='#FF0000' precision="0.00">null</sum>
            <average name="平均值" fillColor='#FFDEAD' dataFillColor='#000080' precision="0.00" percent="0.00%">null</average>
            <MixFormula name="混合计算" fillColor='#FFDEAD' dataFillColor='#000080'>null</MixFormula>
        </structureExt>
        <data>            <!-- 数据源 固定属性  用于指明调用的程序 Mapper 和 方法-->
            <mapper>exportAllMapper</mapper><!-- 由于MYSQL配置了多数据源,整个项目不要有重名的mapper -->
            <method db="MYSQL">selectFollowRate</method>>
            <methodExt db="MYSQL">selectFollowRateExt</methodExt>
            <!-- 当method 查询的数据不足以完成整个报表时 通过methodExt查询其余的数据并根据firstHead的值对应写入excel-->
        </data>
    </F00000>
    <F00001>
        <filesName>每日跟进所有N</filesName>
        <tableName>每日跟进所有N</tableName>
        <sheetName>每日跟进所有N</sheetName>
        <type>2</type>
        <firstHead>reportDay</firstHead>
        <flexHead>empName</flexHead>
        <structure>
            <reportDay name="日期" fillColor='#FFFF00' columnWidth="6">null</reportDay>
            <empName name="分配人">
                <firstFollowValidTotal name="首次跟进有效次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</firstFollowValidTotal>
                <followOne name="首次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followOne>
                <firstFollowValidTwo name="首次有效二次跟进率次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</firstFollowValidTwo>
                <followTwo name="二次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followTwo>
                <followThree name="三次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followThree>
                <total name="总数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</total>
                <firstFollowRate name="首次跟进" fillColor='#93C47D' percent="0.00%" formula="formulaExt001" columnWidth="4">null</firstFollowRate>
                <firstFollowValidRate name="首次有效二次跟进率" fillColor='#93C47D' precision="0.00%" formula="formulaExt001" columnWidth="9">null</firstFollowValidRate>
                <secondFollowRate name="二次跟进" fillColor='#EA9999' percent="0.00%" formula="formulaExt001" columnWidth="4">null</secondFollowRate>
                <thirdlyFollowRate name="三次跟进" fillColor='#EA9999' percent="0.00%" formula="formulaExt001" columnWidth="4">null</thirdlyFollowRate>
            </empName>
        </structure>
        <structureExt>
            <mixFormula name="总计/平均值" fillColor='#00B050'>null</mixFormula>
        </structureExt>
        <data>
            <mapper>exportAllMapper</mapper>>
            <method db="MYSQL">selectNCustFollowDaily</method>>
            <methodExt db="MYSQL">NULL</methodExt>>
        </data>
    </F00001>
    <F00002>
        <filesName>每日跟进其他</filesName>
        <tableName>所有名单其他</tableName>
        <sheetName>所有名单其他</sheetName>
        <type>2</type>
        <firstHead>reportDay</firstHead>
        <flexHead>empName</flexHead>
        <structure>
            <reportDay name="日期" fillColor='#FFFF00' columnWidth="6">null</reportDay>
            <empName name="分配人">
                <followOne name="首次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followOne>
                <followTwo name="二次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followTwo>
                <followThree name="三次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followThree>
                <total name="总数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</total>
                <firstFollowRate name="首次跟进" fillColor='#93C47D' percent="0.00%" formula="formulaExt001" columnWidth="4">null</firstFollowRate>
                <secondFollowRate name="二次跟进" fillColor='#EA9999' percent="0.00%" formula="formulaExt001" columnWidth="4">null</secondFollowRate>
                <thirdlyFollowRate name="三次跟进" fillColor='#EA9999' percent="0.00%" formula="formulaExt001" columnWidth="4">null</thirdlyFollowRate>
            </empName>
        </structure>
        <structureExt>
            <mixFormula name="总计/平均值" fillColor='#00B050'>null</mixFormula>
        </structureExt>
        <data>
            <mapper>exportAllMapper</mapper>>
            <method db="MYSQL">selectOtherCustFollowDaily</method>>
            <methodExt db="MYSQL">null</methodExt>>
        </data>
    </F00002>
    <F00002a>
        <filesName>直接N名单</filesName>
        <tableName>直接N名单</tableName>
        <sheetName>直接N名单</sheetName>
        <type>2</type>
        <firstHead>reportDay</firstHead>
        <flexHead>empName</flexHead>
        <structure>
            <reportDay name="日期" fillColor='#FFFF00' columnWidth="6">null</reportDay>
            <empName name="分配人">
                <firstFollowValidTotal name="首次跟进有效次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</firstFollowValidTotal>
                <followOne name="首次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followOne>
                <firstFollowValidTwo name="首次有效二次跟进率次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</firstFollowValidTwo>
                <followTwo name="二次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followTwo>
                <followThree name="三次跟进次数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</followThree>
                <total name="总数" fillColor='#808080' formula="sum" columnWidth="1" dataFillColor='#808080' dataFontColor='#F2F2F2'>null</total>
                <firstFollowRate name="首次跟进" fillColor='#93C47D' percent="0.00%" formula="formulaExt001" columnWidth="4">null</firstFollowRate>
                <firstFollowValidRate name="首次有效二次跟进率" fillColor='#93C47D' precision="0.00%" formula="formulaExt001" columnWidth="9">null</firstFollowValidRate>
                <secondFollowRate name="二次跟进" fillColor='#EA9999' percent="0.00%" formula="formulaExt001" columnWidth="4">null</secondFollowRate>
                <thirdlyFollowRate name="三次跟进" fillColor='#EA9999' percent="0.00%" formula="formulaExt001" columnWidth="4">null</thirdlyFollowRate>
            </empName>
        </structure>
        <structureExt>
            <mixFormula name="总计/平均值" fillColor='#00B050'>null</mixFormula>
        </structureExt>
        <data>
            <mapper>exportAllMapper</mapper>>
            <method db="MYSQL">selectOtherCustFollowDaily</method>>
            <methodExt db="MYSQL">NULL</methodExt>>
        </data>
    </F00002a>
    <F00003>
        <filesName>名单分配日报表</filesName>
        <tableName>名单分配日报表</tableName>
        <sheetName>名单分配日报表</sheetName>
        <type>2</type>
        <firstHead>reportDay</firstHead>
        <flexHead>empName</flexHead>
        <structure>
            <reportDay name="日期" columnWidth="6">null</reportDay>
            <empName name="员工">
                <custCount name="分配名单" columnWidth="4">null</custCount>
                <custCountFact name="实际名单" columnWidth="4">null</custCountFact>
            </empName>
        </structure>
        <structureExt>
            <sum name="总计" fillColor='#B7E1CD' precision="0">null</sum>
        </structureExt>
        <data>
            <mapper>exportAllMapper</mapper>>
            <method db="MYSQL">selectCustAssignDaily</method>>
            <methodExt db="MYSQL">null</methodExt>>
        </data>
    </F00003>
    <F00004>
        <filesName>总名单分配</filesName>
        <tableName>总名单分配</tableName>
        <sheetName>总名单分配</sheetName>
        <type>2</type>
        <firstHead>reportDay</firstHead>
        <flexHead>empName</flexHead>
        <structure>
            <reportDay name="日期" fillColor='#FFFF00' columnWidth="6">null</reportDay>
            <empName name="员工">
                <custCount name="分配名单" columnWidth="4">null</custCount>
                <custCountFact name="实际名单" columnWidth="4">null</custCountFact>
            </empName>
        </structure>
        <structureExt>
            <sum name="总计" fillColor='#FFB6C1' precision="0">null</sum>
        </structureExt>
        <data>
            <mapper>exportAllMapper</mapper>>
            <method db="MYSQL">selectCustAssignDailyTotal</method>>
            <methodExt db="MYSQL">null</methodExt>>
        </data>
    </F00004>
    <F00005>
        <filesName>名单分配月报表</filesName>
        <tableName>名单分配</tableName>
        <sheetName>名单分配月报表</sheetName>
        <type>2</type>
        <firstHead>empName</firstHead>
        <flexHead>custType</flexHead>
        <structure>
            <empName name="实际名单" fillColor='#FFFF00' columnWidth="4">null</empName>
            <custType name="名单类型" fillColor='#93C47D' columnWidth="6">
                <custCountFact name="数量">null</custCountFact>
            </custType>
        </structure>
        <structureExt>
            <sum name="总计" fillColor='#FFB6C1' precision="0">null</sum>
        </structureExt>
        <data>
            <mapper>exportAllMapper</mapper>>
            <method db="MYSQL">selectCustAssignMonthly</method>>
            <methodExt db="MYSQL">null</methodExt>>
        </data>
    </F00005>
    <!--具有代表性的两层动态表头-->
    <F00006>
        <filesName>业绩统计报表_个人激活目标</filesName>
        <tableName>业绩统计报表_个人激活目标</tableName>
        <sheetName>业绩统计报表_个人激活目标</sheetName>
        <type>3</type>
        <firstHead>empName</firstHead>
        <flexHead>custType</flexHead>
        <flexHead2>accountLevelTranslate</flexHead2>
        <structure>
            <empName name="员工" fillColor='#A9D08E' columnWidth="4">null</empName>
            <activateTotal name="总激活数" fillColor='#B4C6E7' formula="sum" columnWidth="4">null</activateTotal>
            <targetNum name="个人激活目标" fillColor='#808080' formula="sum" columnWidth="6">null</targetNum>
            <reachRate name="达标率" fillColor='#F8CBAD' percent="0.00%" formula="formulaExt002" columnWidth="3">null</reachRate>
            <custType name="名单类型" fillColor='#B4C6E7'>
                <activateLevelTotal name="合计" fillColor='#A9D08E' formula="sum" columnWidth="2">null</activateLevelTotal>
                <accountLevelTranslate name="等级" fillColor='#698B69' columnWidth="4">
                    <accountLevelTranslateTotal name="数量" fillColor='#F8CBAD' formula="sum">null</accountLevelTranslateTotal>
                </accountLevelTranslate>
            </custType>
        </structure>
        <structureExt>
            <mixFormula name="总计" fillColor='#00B050'>null</mixFormula>
        </structureExt>
        <data>
            <mapper>exportAllMapper</mapper>
            <method db="MYSQL">selectCustPerformance</method>>
            <methodExt db="MYSQL">null</methodExt>
        </data>
    </F00006>
</root>
