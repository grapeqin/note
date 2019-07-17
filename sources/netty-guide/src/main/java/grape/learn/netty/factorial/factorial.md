#### 通过factorial示例学到的知识点

###### 一、配置基于SSL的netty transport

1. 通过读取系统变量来配置netty启动相关的参数
2. 单向认证，client信任server证书实例 具体的证书生成步骤请参考 [SSL.md](../SSL.md)
3. 采用了自定义的编解码器(分隔符方式)来避免半包读写问题