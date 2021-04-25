![](https://img.imgdb.cn/item/60858d60d1a9ae528fafd469.jpg)
> Site: <https://developer.android.google.cn/guide/topics/data/backup>

# BackupRestoreApp
Demo that used to show how to use [Android Backup Feature](https://developer.android.google.cn/guide/topics/data/backup) with `BackupAgent` and `BackupManager`.

## Screenshot
![](https://img.imgdb.cn/item/60858e7fd1a9ae528fbf4c8b.png)

## Init data
![](https://img.imgdb.cn/item/60858e9ed1a9ae528fc0f995.jpg)

## Key-value backup mode
![](https://img.imgdb.cn/item/60858edbd1a9ae528fc46122.jpg)

## Auto backup mode
![](https://img.imgdb.cn/item/60858e90d1a9ae528fc03419.jpg)

## Auto backup mode customization
![](https://img.imgdb.cn/item/60858f08d1a9ae528fc70d3c.jpg)

## Backup功能使用总结
![](https://img.imgdb.cn/item/60858e2dd1a9ae528fbaee44.jpg)

## Android Backup Feature's history
| 版本        | 变化内容  |
| --------   | -----|
| Android 1.6     |  加入allowBackup属性默认关闭  |
| Android 2.2     |  开始使用键值对备份模式  |
| Android 6.0     |  开始支持自动备份模式，默认打开allowBackup属性|
| Android 7.0       |  Backup功能将自动备份和恢复用户授予App的权限|
| Android 9.0       |  新增了加密存储备份文件|
| Android 12       |  D2D场景的Backup规则变更和adb backup命令的限制|

## Android Backup attributes
| 相关属性       | 说明   |
| --------   | -----|
| allowBackup     |  是否支持Backup，默认为true |
| backupAgent     |   指定Backup代理进行定制|
| fullBackupContent      |   指定备份规则XML文件|
| restoreAnyVersion     |   是否支持高版本数据恢复到低版本应用，默认为false|
| fullBackupOnly     |   在指定了BackupAgent后仍然采用AutoBackup模式|
| killAfterRestore        |  全系统恢复期后是否终止应用，默认为 true|
| backupInForeground         |   即使应用处于前台也可以对其执行自动备份，默认为false|
| clientSideEncryption        |   只在手机设置密钥的情况下执行备份|
| deviceToDeviceTransfer        |  只在D2D设备间备份的情况下执行备份 |

## :orange_book:　My blog
<https://blog.csdn.net/allisonchen>