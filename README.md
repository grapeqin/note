# Macos10.13下IDEA2018.3.6 安装及破解

##安装IDEA
* 从[这里](https://www.jetbrains.com/idea/download/previous.html) 选择 `2018.3.6 for macOS (dmg)`
下载,完成后双击dmg文件即可完成安装

***

##破解IDEA

* 从[这里](http://idea.lanyus.com/) 选择 [无需使用注册码的破解补丁](http://idea.lanyus.com/jar/JetbrainsIdesCrack-4.2-release-sha1-3323d5d0b82e716609808090d3dc7cb3198b8c4b.jar)
并保存到 IntelliJIDEA.app 中的bin目录下，重命名为`JetbrainsIdesCrack-4.2-release.jar`,之后在 `idea.vmoptions` 的最后一行追加如下内容：
``-javaagent:JetbrainsIdesCrack-4.2-release.jar`` 启动IntelliJIDEA即可完成破解 

***

##FAQ

**Q1.:IDEA无法启动**

  **A**: 从IntelliJIDEA的安装目录运行 `./Contents/MacOS/idea` 命令启动IDEA，即可从控制台看到启动报错信息，可结合报错信息进行排查。

**Q2.:提示破解程序路径不对**

  **A**:默认IntelliJIDEA安装完成后在/Applications/目录下会生成一个以.app结尾的文件夹，可以将该文件名中的不可见字符通过重命名予以替换。
	