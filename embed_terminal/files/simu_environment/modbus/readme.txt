1. 协议识别方法：
    modbus-tcp 的 server 默认固定用 502 端口，所以其中一个端口是 502 的话，就可以初步识别为 modbus 协议。
    如果采用 502 端口的通讯一方有办法知道是 server 的话，TCP 中负载是 modbus 的可能性更高。
    （同样，S7 协议识别采用一方端口是否为 102。如果在TCP包里做一个TPKT和COTP的判断就更准确了。）