/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class IOpenLoopControl {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IOpenLoopControl(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IOpenLoopControl obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_IOpenLoopControl(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean setRefOutput(int j, double v) {
    return yarpJNI.IOpenLoopControl_setRefOutput(swigCPtr, this, j, v);
  }

  public boolean setRefOutputs(SWIGTYPE_p_double v) {
    return yarpJNI.IOpenLoopControl_setRefOutputs(swigCPtr, this, SWIGTYPE_p_double.getCPtr(v));
  }

  public boolean getRefOutput(int j, SWIGTYPE_p_double v) {
    return yarpJNI.IOpenLoopControl_getRefOutput(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(v));
  }

  public boolean getRefOutputs(SWIGTYPE_p_double v) {
    return yarpJNI.IOpenLoopControl_getRefOutputs(swigCPtr, this, SWIGTYPE_p_double.getCPtr(v));
  }

  public boolean getOutput(int j, SWIGTYPE_p_double v) {
    return yarpJNI.IOpenLoopControl_getOutput(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(v));
  }

  public boolean getOutputs(SWIGTYPE_p_double v) {
    return yarpJNI.IOpenLoopControl_getOutputs(swigCPtr, this, SWIGTYPE_p_double.getCPtr(v));
  }

  public boolean setOpenLoopMode() {
    return yarpJNI.IOpenLoopControl_setOpenLoopMode(swigCPtr, this);
  }

}
