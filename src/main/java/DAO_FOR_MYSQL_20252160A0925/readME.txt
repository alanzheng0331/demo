1.这个包用来创建 Mysql语句
他的作用是 把 括号中 输入过来的数据 整合成为 Mysql语句 然后输入到 util[数据库交互处理 类]


2.这个 类依据的思想是：
浏览器请求 → Controller（TestServer）→ Service（UserServiceImpl）→ Dao（UserDao）→ Util（DBUtil）→ MySQL数据库
                                                                 ↓
浏览器响应 ← Controller（TestServer）← Service（UserServiceImpl）← Dao（UserDao）← 数据库查询结果Util

Controller 输入的数据处理
Service 链接 Dao 和 Controller
Dao 创建 Mysql语句
Util 数据库的交互 【开数据库 ——>输入数据库 语句 -》【返回数据到Dao】 --》关闭数据库】


有疑惑请修改 或删除这个数据包

模板：TestInterface_DAO_SQL