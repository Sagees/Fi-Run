# ReadMe

#ANDROID 

1.新建工程

Android Studio配置

targetSdkVersion版本选择 21
将minewBeaconScan.jar包放入道libs文件夹下,然后在当前工程下的build.gradle文件配置项中的dependencies新增内容,如下compile files('libs/minewBeaconScan.jar')

Eclipse配置

targetSdkVersion版本选择 21
将scanBeacon.jar包放入道libs文件夹下,右击工程propeties,选择Java build Path，在Library选项中添加minewBeaconScan依赖关系

targetSdkVersion版本大于等于23时，需要动态申请权限
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>

当前SDK需要的权限：
	<uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
添加service和receiver,如下
	<service android:name="com.minew.beacon.ScanService"/>
	<receiver android:name="com.minew.beacon.BluetoothChangedReceiver">
            <intent-filter>
                <action android:name="android.bluetooth.adapter.action.STATE_CHANGED"/>
            </intent-filter>
    </receiver>
具体实现细节，请参考demo。
```

## 开始使用

MinewBeaconManager（以下简称Manager）类用来发起和停止扫描周边的蓝牙设备，它将为每一个蓝牙设备生成一个对应的Minewbeacon（以下简称Beacon）实例。当Manager扫描到蓝牙设备时，它会持续监听蓝牙设备的数据更新以及这些设备的进出状态（十秒内没有数据更新的设备被认为是消失设备，第一次扫描到或者消失后再次扫描到的设备被认为是出现设备），同时，Manager类还可以监听蓝牙状态的改变并且回调给对应的代理实例。以下是相关代码。

1.获取设备实例，配置代理:

```android
MinewBeaconManager mMinewBeaconManager = MinewBeaconManager.getInstance(this);
mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {})；
```

2.扫描设备

```android
mMinewBeaconManager.startScan();
```

3.通过回调获取设备数据更新

```android
// 获取持续的设备数据更新，此方法每1秒回调一次，当且仅当扫描到设备时才会回调。
- public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                });

```

 4.获取beacon实例的数据

​    通过-MinewBeacon.getBeaconValue:(BeaconValueIndex)index方法来获取设备数据，为了数据通用性，我们使用了MinewBeaconValue类（以下简称value）来统一所有的数据类型，value可以对应字符串、整型、浮点型、布尔型数据。每一个index对应一个值，如下示例：

```android
在列表中通过Beacon集合展示数据

// 获取uuid
mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue();
// 获取major
mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue();

// 获取minor
mMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
```

附表：ValueIndex以及对应数据类型

| index                         | 数据名     | 类型          | 备注   |
| ----------------------------- | ------- | ----------- | ---- |
| BeaconValueIndex_UUID         | uuid    | stringValue |      |
| BeaconValueIndex_Name         | 设备名     | stringValue |      |
| BeaconValueIndex_Major        | major   | intValue    |      |
| BeaconValueIndex_Minor        | minor   | intValue    |      |
| BeaconValueIndex_WechatId     | 微信设备id  | intValue    | 部分支持 |
| BeaconValueIndex_Mac          | mac地址   | stringValue | 部分支持 |
| BeaconValueIndex_RSSI         | rssi    | intValue    |      |
| BeaconValueIndex_BatteryLevel | 电池电量    | intValue    |      |
| BeaconValueIndex_Temperature  | 温度      | floatValue  | 部分支持 |
| BeaconValueIndex_Humidity     | 湿度      | floatValue  | 部分支持 |
| BeaconValueIndex_Txpower      | txPower | intValue    |      |
| BeaconValueIndex_InRange      | 是否在范围内  | boolValue   |      |
| BeaconValueIndex_Connectable  | 是否可连接   | boolValue   |      |

你只需要按照对应的index来获取想要的数据就可以了。 

至此，就完成了SDK的基础使用，如果需要更多支持可以参照如下示例：

监听设备的进出状态：

```Android
// 如果设备8秒内没有数据更新，此设备被认为是已经消失，消失的设备将会在此回调，此方法每5秒一个周期，
- public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                for (MinewBeacon minewBeacon : minewBeacons) {
                    String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                    Toast.makeText(getApplicationContext(), deviceName + "  out range", Toast.LENGTH_SHORT).show();
                }
            }
}

// 第一次被扫描到的或者消失后重新被扫描到的设备被认为是新出现设备，新出现的设备将会在此回调，此方法每3秒一个周期。
- @Override
  public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

  }
```

监听蓝牙模块状态更新

蓝牙状态一共有三种：

BluetoothState：BluetoothStatePowerOn（蓝牙开启），BluetoothStatePowerOff（蓝牙关闭），BluetoothStateNotSupport（蓝牙不支持ble）

```android
// 因用户或者系统改变的蓝牙开关，都能在以下方法监听到：
- @Override
  public void onUpdateBluetoothState(BluetoothState state) {

  }
```

当然，你也可以手动检查蓝牙状态：

```Android
- BluetoothState bluetoothState = mMinewDeviceManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
```



## 类说明

### MinewBeacon

设备类，每一个设备对应一个此类实例，此类仅有一个方法，通过对应的Index来获取设备的信息：
- public MinewBeaconValue getBeaconValue(NS_ENUM ns_enum);

	例如获取某个设备实例的UUID：
  	aBeacon getBeaconValue(NS_ENUM.MinewBeaconValueIndex_UUID).getStringValue;

	// 这里是设备数据的对应枚举；
 	// !!!:必须按照每个设备的数据类型来获取数据。
	public enum BeaconValueIndex {
    MinewBeaconValueIndex_UUID,  // UUID
    MinewBeaconValueIndex_Major,   // major
    MinewBeaconValueIndex_Minor,   // minor
    MinewBeaconValueIndex_Name,   // 设备名称
    MinewBeaconValueIndex_RSSI,   // RSSI
    MinewBeaconValueIndex_BatteryLevel,  // 电池电量
    //MinewBeaconValueIndex_Temperature,  // 温度 暂不提供数据
	//MinewBeaconValueIndex_Humidity,   // 湿度 暂不提供数据
    MinewBeaconValueIndex_TxPower,  // TxPower
    MinewBeaconValueIndex_InRage  // 是否在范围内
	}

### MinewBeaconValue

// 设备数据类：MinewBeaconValue，从设备获取的所有数据均得到此类实例，请严格按照对应数据类型获取数据
	//  获取整形数据
    private int intValue;

    // 获取浮点型数据
    private float floatValue;

    // 获取字符串型数据
    private String stringValue;

    //获取boolean型数据
    private boolean bool;

    // 获取16进制data型数据
    private byte[] dataValue;

### 蓝牙状态枚举类：
- public enum  BluetoothState {
    BluetoothStatePowerOn, //蓝牙已开启
    BluetoothStatePowerOff, //蓝牙已关闭
    BluetoothStateNotSupported,//不支持蓝牙4.0
	}

##MinewBeaconManagerListener

	设备管理类回调接口。
	// 发现了新的设备，该函数每3秒回调一次
    void onAppearBeacons(List<MinewBeacon> minewBeacons);

    // 设备消失的情况下，每秒钟回调一次。
    void onDisappearBeacons(List<MinewBeacon> minewBeacons);

    //定期回调数据更新函数，每秒钟回调一次
    void onRangeBeacons(List<MinewBeacon> minewBeacons);

	// 蓝牙状态更新
    void onUpdateState(BluetoothState state);


@end

##MinewBeaconManager
设备管理类，用来扫描设备以及更新设备状态等。
// 设备管理类代理
	public void setDeviceManagerDelegateListener(MinewBeaconManagerListener minewBeaconManagerListener) {
    }
// 单例
- public static MinewBeaconManager getInstance(Context context) {}

// 获取蓝牙状态
- public BluetoothState checkBluetoothState() {}

// 开始扫描设备
- public void startScan() throws Exception {}

// 停止扫描设备
- public void stopScan() {}


// 扫描到的所有设备
-public static List<MinewBeacon> scannedBeacons;

// 当前在范围内的设备
-public static List<MinewBeacon> inRangeBeacons;

@end

#Change log
  2016.11.29 添加对温湿度传感器数据的支持。





