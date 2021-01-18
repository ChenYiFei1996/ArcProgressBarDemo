# ArcProgressBarDemo
# 安卓圆形进度条控件实现及demo
*目前发现在安卓5.1上存在以下问题：
①绘制时角度有偏差
②完成部分与未完成部分有空白*

## 控件参数及含义
|*参数名*|*参数取值*|*参数含义*|
| --------| -----:| :----:|
|borderWidth|dimension|绘制圆弧的宽度|
|unProgressColor|color|无进度部分的颜色|
|progressColor|color|有进度部分的颜色|
|tickWidth|dimension|刻度进度条的线条宽度|
|tickDensity|integer|刻度进度条之间间隔的角度|
|bgShow|boolean|是否展示刻度进度条的背景|
|arcBgColor|color|刻度进度条背景颜色|
|barRadius|dimension|圆弧半径|
|degree|integer|不绘制圆弧的角度|
|arcCapRound|boolean|是否使用圆形笔帽|
|progressStyle|自定义类型tick/arc|进度条类型（tick为刻度进度条，arc为圆形进度条）|
