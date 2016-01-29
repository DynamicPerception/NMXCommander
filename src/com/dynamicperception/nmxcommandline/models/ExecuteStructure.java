package com.dynamicperception.nmxcommandline.models;

public interface ExecuteStructure {
	<T>T executeThis();
	<T>T executeThis(String dataOrMotor);
	<T>T executeThis(String motor, String data);
}
