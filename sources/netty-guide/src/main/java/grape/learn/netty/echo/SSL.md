##### 利用JDK工具keytool 生成自定义签名

1、单向认证 客户端验证服务端证书真实性

- 生成服务器端秘钥对

```shell
keytool -genkeypair -alias nettyserver -keyalg rsa -keysize 1024 -sigalg sha256withrsa -keypass nettyserver -keystore ~/mynettyserver

输入密钥库口令:  
再次输入新口令: 
您的名字与姓氏是什么?
  [Unknown]:  qzy
您的组织单位名称是什么?
  [Unknown]:  yunnex
您的组织名称是什么?
  [Unknown]:  yunnex
您所在的城市或区域名称是什么?
  [Unknown]:  sz
您所在的省/市/自治区名称是什么?
  [Unknown]:  gd
该单位的双字母国家/地区代码是什么?
  [Unknown]:  cn
CN=qzy, OU=yunnex, O=yunnex, L=sz, ST=gd, C=cn是否正确?
  [否]:  y
```

- 导出服务器端密钥证书

```shell
keytool -exportcert -alias nettyserver -keystore ~/mynettyserver -storepass nettyserver -file ~/mynettyserver.cert
```

- 将服务器端证书导入到客户端的信任仓库中

```shell
keytool -importcert -alias nettyserver -keystore ~/mynettyclienttrust.keystore -storepass nettyclient -file ~/mynettyserver.cert
```

- 实现代码请参考[OnewaySslServer](OnewaySslServer.java)和[OnewaySslClient](OnewaySslClient.java)

2、双向认证 服务器端也要验证客户端的证书真实性

- 生成客户端密钥对

```shell
keytool -genkeypair -alias nettyclient -keyalg rsa -keysize 1024 -sigalg sha256withrsa -keypass nettyclient -keystore ~/mynettyclient

输入密钥库口令:  
再次输入新口令: 
您的名字与姓氏是什么?
  [Unknown]:  qzy
您的组织单位名称是什么?
  [Unknown]:  yunnex
您的组织名称是什么?
  [Unknown]:  yunnex
您所在的城市或区域名称是什么?
  [Unknown]:  sz
您所在的省/市/自治区名称是什么?
  [Unknown]:  gd
该单位的双字母国家/地区代码是什么?
  [Unknown]:  cn
CN=qzy, OU=yunnex, O=yunnex, L=sz, ST=gd, C=cn是否正确?
  [否]:  y
```

- 导出客户端密钥证书

```shell
keytool -exportcert -alias nettyclient -keystore ~/mynettyclient -storepass nettyclient -file ~/mynettyclient.cert
```

- 将客户端证书导入到服务端的信任仓库中

```shell
keytool -importcert -alias nettyclient -keystore ~/mynettyservertrust.keystore -storepass nettyserver -file ~/mynettyclient.cert
```

- 实现代码请参考[SslServer](SslServer.java)和[SslClient](SslClient.java)