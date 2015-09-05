package com.alecive.yarpdroid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import example.yarpcommon.sensor.OrientationSensor;

public class AllSensorManager {

	private OrientationSensor orientationSensor;

	// Sensor manager
	private SensorManager mSensorManager = null;
	private LocationManager locationManager = null;
	
	// Sensors
	private Sensor mSensorAccelerometer = null;
	private Sensor mSensorGravity = null;
	private Sensor mSensorGyroscope = null;
	private Sensor mSensorLight = null;	
	private Sensor mSensorLinearAcceleration = null;
	private Sensor mSensorMagneticField = null;
	private Sensor mSensorRotationVector = null;
	private List<String> locationProviders = null;
	
	// Sensor event listeners
	private SensorEventListener mSensorAccelerometerListener = null;
	private SensorEventListener mSensorGravityListener = null;
	private SensorEventListener mSensorGyroscopeListener = null;
	private SensorEventListener mSensorLightListener = null;	
	private SensorEventListener mSensorLinearAccelerationListener = null;
	private SensorEventListener mSensorMagneticFieldListener = null;
	private SensorEventListener mSensorRotationVectorListener = null;
	
	// Location event listener
	private LocationListener locationListener = null;
	
	// data
	private float[] lastAccelerometerValues;
	private float[] lastGravityValues;
	private float[] lastGyroscopeValues;
	private float[] lastLightValues;
	private float[] lastLinearAccelerationValues;
	private float[] lastMagneticFieldValues;
	private float[] lastRotationVectorValues;
	private double[] lastLocationValues;
	private long lastLocationTimestamp;
	
	// lock sync objects
	private Object accelerometerLock;
	private Object gravityLock;
	private Object gyroscopeLock;
	private Object lightLock;
	private Object linearAccelerationLock;
	private Object magneticFieldLock;
	private Object rotationVectorLock;
	private Object locationLock;
	
	private SimpleDateFormat formatter;
	private int sensorDelayRate;
	private int FPS;
	
	public AllSensorManager(Context context, int FPS) {
		this.FPS = FPS;
		sensorDelayRate = 1000000/FPS;
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		formatter = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS", Locale.ENGLISH);
		initialiseData();
		initialiseSensors();
		initialiseListeners();		
		initialiseLocks();

		orientationSensor = new OrientationSensor();
	}

	private void initialiseLocks() {
		accelerometerLock = new Object();
		gravityLock = new Object();
		gyroscopeLock = new Object();
		lightLock = new Object();
		linearAccelerationLock = new Object();
		magneticFieldLock = new Object();
		rotationVectorLock = new Object();
		locationLock = new Object();
	}

	private void initialiseData() {
		lastAccelerometerValues = new float[3];
		lastGravityValues = new float[3];
		lastGyroscopeValues = new float[3];
		lastLightValues = new float[1];
		lastLinearAccelerationValues = new float[3];
		lastMagneticFieldValues = new float[3];
		lastRotationVectorValues = new float[4];
		lastLocationValues = new double[5];
		for (int i = 0; i < lastLocationValues.length; i++) {
			lastLocationValues[i] = 0;
		}
		lastLocationTimestamp = 0;
	}

	private void initialiseListeners() {
		initialiseAccelerometerSensor();
		initialiseGravityListener();
		initialiseGyroscopeListener();
		initialiseLightListener();
		initialiseLinearAccelerationListener();
		initialiseMagneticFieldListener();
		initialiseRotationVectorListener();
		initialiseLocationListener();
	}

	private void initialiseLocationListener() {
		locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			
			@Override
			public void onProviderEnabled(String provider) {
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			}
			
			@Override
			public void onLocationChanged(Location location) {
				synchronized(locationLock) {
					if (location.getTime() > lastLocationTimestamp) {
						lastLocationValues[0] = location.getLatitude();
						lastLocationValues[1] = location.getLongitude();
						lastLocationValues[2] = location.getAltitude();
						lastLocationValues[3] = location.getBearing();
						lastLocationValues[4] = location.getSpeed();
						lastLocationTimestamp = location.getTime();
					}	
				}
			}
		};
	}

	private void initialiseAccelerometerSensor() {
		mSensorAccelerometerListener = new SensorEventListener() {			
			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized(accelerometerLock) {
					lastAccelerometerValues = event.values;
					orientationSensor.setAccelerometerData(lastAccelerometerValues);
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}

	private void initialiseGravityListener() {
		mSensorGravityListener = new SensorEventListener() {			
			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized(gravityLock) {
					lastGravityValues = event.values;	
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}
	
	private void initialiseGyroscopeListener() {
		mSensorGyroscopeListener = new SensorEventListener() {			
			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized(gyroscopeLock) {
					lastGyroscopeValues = event.values;
					orientationSensor.setGyroscopeData(lastGyroscopeValues, event.timestamp);
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}
	
	private void initialiseLightListener() {
		mSensorLightListener = new SensorEventListener() {			
			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized(lightLock) {
					lastLightValues = event.values;		
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}
	
	private void initialiseLinearAccelerationListener() {
		mSensorLinearAccelerationListener = new SensorEventListener() {			
			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized(linearAccelerationLock) {
					lastLinearAccelerationValues = event.values;	
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}
	
	private void initialiseMagneticFieldListener() {
		mSensorMagneticFieldListener = new SensorEventListener() {			
			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized(magneticFieldLock) {
					lastMagneticFieldValues = event.values;
					orientationSensor.setMagneticData(lastMagneticFieldValues);
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}
	
	private void initialiseRotationVectorListener() {
		mSensorRotationVectorListener = new SensorEventListener() {			
			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized(rotationVectorLock) {
					lastRotationVectorValues = event.values;
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}
	
	private void initialiseSensors() {
		mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mSensorLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mSensorRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);	
		locationProviders = locationManager.getProviders(criteria, true /* enabledOnly */);		
	}

	public void registerLocationSensor() {
		for (String provider : locationProviders) {
		    locationManager.requestLocationUpdates(provider, 1000*FPS, 1, locationListener);
		}		
	}
	
	public void unregisterLocationSensor() {
		locationManager.removeUpdates(locationListener);		
	}
	
	public void registerSensors() {
		mSensorManager.registerListener(mSensorAccelerometerListener, mSensorAccelerometer, sensorDelayRate);
		mSensorManager.registerListener(mSensorGravityListener, mSensorGravity, sensorDelayRate);
		mSensorManager.registerListener(mSensorGyroscopeListener, mSensorGyroscope, sensorDelayRate);
		mSensorManager.registerListener(mSensorLightListener, mSensorLight, sensorDelayRate);
		mSensorManager.registerListener(mSensorLinearAccelerationListener, mSensorLinearAcceleration, sensorDelayRate);
		mSensorManager.registerListener(mSensorMagneticFieldListener, mSensorMagneticField, sensorDelayRate);
		mSensorManager.registerListener(mSensorRotationVectorListener, mSensorRotationVector, sensorDelayRate);			
	}

	public void registerOrientationSensors() {
		mSensorManager.registerListener(mSensorAccelerometerListener, mSensorAccelerometer, sensorDelayRate);
		mSensorManager.registerListener(mSensorGyroscopeListener, mSensorGyroscope, sensorDelayRate);
		mSensorManager.registerListener(mSensorMagneticFieldListener, mSensorMagneticField, sensorDelayRate);
	}

	public void unregisterSensors() {
		mSensorManager.unregisterListener(mSensorAccelerometerListener, mSensorAccelerometer);
		mSensorManager.unregisterListener(mSensorGravityListener, mSensorGravity);
		mSensorManager.unregisterListener(mSensorGyroscopeListener, mSensorGyroscope);
		mSensorManager.unregisterListener(mSensorLightListener, mSensorLight);
		mSensorManager.unregisterListener(mSensorLinearAccelerationListener, mSensorLinearAcceleration);
		mSensorManager.unregisterListener(mSensorMagneticFieldListener, mSensorMagneticField);
		mSensorManager.unregisterListener(mSensorRotationVectorListener, mSensorRotationVector);
	}

	public void unregisterOrientationSensors() {
		mSensorManager.unregisterListener(mSensorAccelerometerListener, mSensorAccelerometer);
		mSensorManager.unregisterListener(mSensorGyroscopeListener, mSensorGyroscope);
		mSensorManager.unregisterListener(mSensorMagneticFieldListener, mSensorMagneticField);
	}

	public String getOrientationData() {
		String value = "";
		synchronized(accelerometerLock) {
			synchronized(gyroscopeLock) {
				// using direct string concat as the compiler is able to best optimize this code for best performance
				value = lastAccelerometerValues[0] + " " +
						lastAccelerometerValues[1] + " " +
						lastAccelerometerValues[2] + " " +
						lastGyroscopeValues[0] + " " +
						lastGyroscopeValues[1] + " " +
						lastGyroscopeValues[2];
			}
		}
		return value;
	}

	public float[] getOrientationDataAsFloats() {
		float[] value = new float[9];
		synchronized(accelerometerLock) {
			synchronized(gyroscopeLock) {
				synchronized(magneticFieldLock) {
					float[] orientation = orientationSensor.getOrientationFromFusedSensors();
					value[0] = lastAccelerometerValues[0];
					value[1] = lastAccelerometerValues[1];
					value[2] = lastAccelerometerValues[2];
					value[3] = lastGyroscopeValues[0];
					value[4] = lastGyroscopeValues[1];
					value[5] = lastGyroscopeValues[2];
					value[6] = orientation[0];
					value[7] = orientation[1];
					value[8] = orientation[2];
				}
			}
		}
		return value;
	}

	public String getData() {
		//unregisterSensors();
		
		String value = "";
		synchronized(accelerometerLock) {
			synchronized(gravityLock) {
				synchronized(gyroscopeLock) {
					synchronized(lightLock) {
						synchronized(linearAccelerationLock) {
							synchronized(magneticFieldLock) {							
								synchronized(rotationVectorLock) {
									synchronized(locationLock) {
										// using direct string concat as the compiler is able to best optimize this code for best performance
										value = formatter.format(new Date().getTime()) + "\t" +
												lastAccelerometerValues[0] + "\t" +
												lastAccelerometerValues[1] + "\t" +
												lastAccelerometerValues[2] + "\t" +
												lastGravityValues[0] + "\t" +
												lastGravityValues[1] + "\t" +
												lastGravityValues[2] + "\t" +
												lastGyroscopeValues[0] + "\t" +
												lastGyroscopeValues[1] + "\t" +
												lastGyroscopeValues[2] + "\t" +
												lastLightValues[0] + "\t" +
												lastLinearAccelerationValues[0] + "\t" +
												lastLinearAccelerationValues[1] + "\t" +
												lastLinearAccelerationValues[2] + "\t" +
												lastMagneticFieldValues[0] + "\t" +
												lastMagneticFieldValues[1] + "\t" +
												lastMagneticFieldValues[2] + "\t" +
												lastRotationVectorValues[0] + "\t" +
												lastRotationVectorValues[1] + "\t" +
												lastRotationVectorValues[2] + "\t" +
												lastRotationVectorValues[3] + "\t" +
												lastLocationValues[0] + "\t" +
												lastLocationValues[1] + "\t" +
												lastLocationValues[2] + "\t" +
												lastLocationValues[3] + "\t" +
												lastLocationValues[4];									
									}
								}
							}
						}
					}
				}
			}
		}		
		
		//registerSensors();
		
		return value;
	}
	
	public String getHeader() {
		String value = "Timestamp\t" +
				"Accelerometer1\t" +
				"Accelerometer2\t" +
				"Accelerometer3\t" +
				"Gravity1\t" +
				"Gravity2\t" +
				"Gravity3\t" +
				"Gyroscope1\t" +
				"Gyroscope2\t" +
				"Gyroscope3\t" +
				"Light\t" +
				"LinearAcceleration1\t" +
				"LinearAcceleration2\t" +
				"LinearAcceleration3\t" +
				"MagneticField1\t" +
				"MagneticField2\t" +
				"MagneticField3\t" +
				"RotationVector1\t" +
				"RotationVector2\t" +
				"RotationVector3\t" +
				"RotationVector4\t" +
				"Latitude\t" +
				"Longitude\t" +
				"Altitude\t" +
				"Bearing\t" +
				"Speed";
		return value;		
	}

}
