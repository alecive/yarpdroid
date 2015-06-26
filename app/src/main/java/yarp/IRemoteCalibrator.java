/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class IRemoteCalibrator {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IRemoteCalibrator(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IRemoteCalibrator obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_IRemoteCalibrator(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean setCalibratorDevice(IRemoteCalibrator dev) {
    return yarpJNI.IRemoteCalibrator_setCalibratorDevice(swigCPtr, this, IRemoteCalibrator.getCPtr(dev), dev);
  }

  public IRemoteCalibrator getCalibratorDevice() {
    long cPtr = yarpJNI.IRemoteCalibrator_getCalibratorDevice(swigCPtr, this);
    return (cPtr == 0) ? null : new IRemoteCalibrator(cPtr, false);
  }

  public boolean isCalibratorDevicePresent(SWIGTYPE_p_bool isCalib) {
    return yarpJNI.IRemoteCalibrator_isCalibratorDevicePresent(swigCPtr, this, SWIGTYPE_p_bool.getCPtr(isCalib));
  }

  public void releaseCalibratorDevice() {
    yarpJNI.IRemoteCalibrator_releaseCalibratorDevice(swigCPtr, this);
  }

  public boolean calibrateSingleJoint(int j) {
    return yarpJNI.IRemoteCalibrator_calibrateSingleJoint(swigCPtr, this, j);
  }

  public boolean calibrateWholePart() {
    return yarpJNI.IRemoteCalibrator_calibrateWholePart(swigCPtr, this);
  }

  public boolean homingSingleJoint(int j) {
    return yarpJNI.IRemoteCalibrator_homingSingleJoint(swigCPtr, this, j);
  }

  public boolean homingWholePart() {
    return yarpJNI.IRemoteCalibrator_homingWholePart(swigCPtr, this);
  }

  public boolean parkSingleJoint(int j, boolean _wait) {
    return yarpJNI.IRemoteCalibrator_parkSingleJoint__SWIG_0(swigCPtr, this, j, _wait);
  }

  public boolean parkSingleJoint(int j) {
    return yarpJNI.IRemoteCalibrator_parkSingleJoint__SWIG_1(swigCPtr, this, j);
  }

  public boolean parkWholePart() {
    return yarpJNI.IRemoteCalibrator_parkWholePart(swigCPtr, this);
  }

  public boolean quitCalibrate() {
    return yarpJNI.IRemoteCalibrator_quitCalibrate(swigCPtr, this);
  }

  public boolean quitPark() {
    return yarpJNI.IRemoteCalibrator_quitPark(swigCPtr, this);
  }

}
