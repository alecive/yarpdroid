/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class IPositionControl {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IPositionControl(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IPositionControl obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_IPositionControl(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean getAxes(SWIGTYPE_p_int ax) {
    return yarpJNI.IPositionControl_getAxes__SWIG_0(swigCPtr, this, SWIGTYPE_p_int.getCPtr(ax));
  }

  public boolean setPositionMode() {
    return yarpJNI.IPositionControl_setPositionMode(swigCPtr, this);
  }

  public boolean positionMove(int j, double ref) {
    return yarpJNI.IPositionControl_positionMove__SWIG_0(swigCPtr, this, j, ref);
  }

  public boolean positionMove(SWIGTYPE_p_double refs) {
    return yarpJNI.IPositionControl_positionMove__SWIG_1(swigCPtr, this, SWIGTYPE_p_double.getCPtr(refs));
  }

  public boolean relativeMove(int j, double delta) {
    return yarpJNI.IPositionControl_relativeMove__SWIG_0(swigCPtr, this, j, delta);
  }

  public boolean relativeMove(SWIGTYPE_p_double deltas) {
    return yarpJNI.IPositionControl_relativeMove__SWIG_1(swigCPtr, this, SWIGTYPE_p_double.getCPtr(deltas));
  }

  public boolean checkMotionDone(int j, SWIGTYPE_p_bool flag) {
    return yarpJNI.IPositionControl_checkMotionDone__SWIG_0(swigCPtr, this, j, SWIGTYPE_p_bool.getCPtr(flag));
  }

  public boolean checkMotionDone(SWIGTYPE_p_bool flag) {
    return yarpJNI.IPositionControl_checkMotionDone__SWIG_1(swigCPtr, this, SWIGTYPE_p_bool.getCPtr(flag));
  }

  public boolean setRefSpeed(int j, double sp) {
    return yarpJNI.IPositionControl_setRefSpeed(swigCPtr, this, j, sp);
  }

  public boolean setRefSpeeds(SWIGTYPE_p_double spds) {
    return yarpJNI.IPositionControl_setRefSpeeds__SWIG_0(swigCPtr, this, SWIGTYPE_p_double.getCPtr(spds));
  }

  public boolean setRefAcceleration(int j, double acc) {
    return yarpJNI.IPositionControl_setRefAcceleration(swigCPtr, this, j, acc);
  }

  public boolean setRefAccelerations(SWIGTYPE_p_double accs) {
    return yarpJNI.IPositionControl_setRefAccelerations(swigCPtr, this, SWIGTYPE_p_double.getCPtr(accs));
  }

  public boolean getRefSpeed(int j, SWIGTYPE_p_double ref) {
    return yarpJNI.IPositionControl_getRefSpeed__SWIG_0(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(ref));
  }

  public boolean getRefSpeeds(SWIGTYPE_p_double spds) {
    return yarpJNI.IPositionControl_getRefSpeeds__SWIG_0(swigCPtr, this, SWIGTYPE_p_double.getCPtr(spds));
  }

  public boolean getRefAcceleration(int j, SWIGTYPE_p_double acc) {
    return yarpJNI.IPositionControl_getRefAcceleration__SWIG_0(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(acc));
  }

  public boolean getRefAccelerations(SWIGTYPE_p_double accs) {
    return yarpJNI.IPositionControl_getRefAccelerations__SWIG_0(swigCPtr, this, SWIGTYPE_p_double.getCPtr(accs));
  }

  public boolean stop(int j) {
    return yarpJNI.IPositionControl_stop__SWIG_0(swigCPtr, this, j);
  }

  public boolean stop() {
    return yarpJNI.IPositionControl_stop__SWIG_1(swigCPtr, this);
  }

  public int getAxes() {
    return yarpJNI.IPositionControl_getAxes__SWIG_1(swigCPtr, this);
  }

  public boolean positionMove(DVector data) {
    return yarpJNI.IPositionControl_positionMove__SWIG_2(swigCPtr, this, DVector.getCPtr(data), data);
  }

  public boolean relativeMove(DVector data) {
    return yarpJNI.IPositionControl_relativeMove__SWIG_2(swigCPtr, this, DVector.getCPtr(data), data);
  }

  public boolean setRefSpeeds(DVector data) {
    return yarpJNI.IPositionControl_setRefSpeeds__SWIG_1(swigCPtr, this, DVector.getCPtr(data), data);
  }

  public boolean getRefSpeed(int j, DVector data) {
    return yarpJNI.IPositionControl_getRefSpeed__SWIG_1(swigCPtr, this, j, DVector.getCPtr(data), data);
  }

  public boolean getRefSpeeds(DVector data) {
    return yarpJNI.IPositionControl_getRefSpeeds__SWIG_1(swigCPtr, this, DVector.getCPtr(data), data);
  }

  public boolean getRefAcceleration(int j, DVector data) {
    return yarpJNI.IPositionControl_getRefAcceleration__SWIG_1(swigCPtr, this, j, DVector.getCPtr(data), data);
  }

  public boolean getRefAccelerations(DVector data) {
    return yarpJNI.IPositionControl_getRefAccelerations__SWIG_1(swigCPtr, this, DVector.getCPtr(data), data);
  }

  public boolean checkMotionDone() {
    return yarpJNI.IPositionControl_checkMotionDone__SWIG_2(swigCPtr, this);
  }

  public boolean checkMotionDone(BVector flag) {
    return yarpJNI.IPositionControl_checkMotionDone__SWIG_3(swigCPtr, this, BVector.getCPtr(flag), flag);
  }

  public boolean checkMotionDone(int i, BVector flag) {
    return yarpJNI.IPositionControl_checkMotionDone__SWIG_4(swigCPtr, this, i, BVector.getCPtr(flag), flag);
  }

  public boolean isMotionDone(int i) {
    return yarpJNI.IPositionControl_isMotionDone__SWIG_0(swigCPtr, this, i);
  }

  public boolean isMotionDone() {
    return yarpJNI.IPositionControl_isMotionDone__SWIG_1(swigCPtr, this);
  }

}
