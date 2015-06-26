/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class IPidControlRaw {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IPidControlRaw(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IPidControlRaw obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_IPidControlRaw(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean setPidRaw(int j, SWIGTYPE_p_Pid pid) {
    return yarpJNI.IPidControlRaw_setPidRaw(swigCPtr, this, j, SWIGTYPE_p_Pid.getCPtr(pid));
  }

  public boolean setPidsRaw(SWIGTYPE_p_Pid pids) {
    return yarpJNI.IPidControlRaw_setPidsRaw(swigCPtr, this, SWIGTYPE_p_Pid.getCPtr(pids));
  }

  public boolean setReferenceRaw(int j, double ref) {
    return yarpJNI.IPidControlRaw_setReferenceRaw(swigCPtr, this, j, ref);
  }

  public boolean setReferencesRaw(SWIGTYPE_p_double refs) {
    return yarpJNI.IPidControlRaw_setReferencesRaw(swigCPtr, this, SWIGTYPE_p_double.getCPtr(refs));
  }

  public boolean setErrorLimitRaw(int j, double limit) {
    return yarpJNI.IPidControlRaw_setErrorLimitRaw(swigCPtr, this, j, limit);
  }

  public boolean setErrorLimitsRaw(SWIGTYPE_p_double limits) {
    return yarpJNI.IPidControlRaw_setErrorLimitsRaw(swigCPtr, this, SWIGTYPE_p_double.getCPtr(limits));
  }

  public boolean getErrorRaw(int j, SWIGTYPE_p_double err) {
    return yarpJNI.IPidControlRaw_getErrorRaw(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(err));
  }

  public boolean getErrorsRaw(SWIGTYPE_p_double errs) {
    return yarpJNI.IPidControlRaw_getErrorsRaw(swigCPtr, this, SWIGTYPE_p_double.getCPtr(errs));
  }

  public boolean getOutputRaw(int j, SWIGTYPE_p_double out) {
    return yarpJNI.IPidControlRaw_getOutputRaw(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(out));
  }

  public boolean getOutputsRaw(SWIGTYPE_p_double outs) {
    return yarpJNI.IPidControlRaw_getOutputsRaw(swigCPtr, this, SWIGTYPE_p_double.getCPtr(outs));
  }

  public boolean getPidRaw(int j, SWIGTYPE_p_Pid pid) {
    return yarpJNI.IPidControlRaw_getPidRaw(swigCPtr, this, j, SWIGTYPE_p_Pid.getCPtr(pid));
  }

  public boolean getPidsRaw(SWIGTYPE_p_Pid pids) {
    return yarpJNI.IPidControlRaw_getPidsRaw(swigCPtr, this, SWIGTYPE_p_Pid.getCPtr(pids));
  }

  public boolean getReferenceRaw(int j, SWIGTYPE_p_double ref) {
    return yarpJNI.IPidControlRaw_getReferenceRaw(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(ref));
  }

  public boolean getReferencesRaw(SWIGTYPE_p_double refs) {
    return yarpJNI.IPidControlRaw_getReferencesRaw(swigCPtr, this, SWIGTYPE_p_double.getCPtr(refs));
  }

  public boolean getErrorLimitRaw(int j, SWIGTYPE_p_double limit) {
    return yarpJNI.IPidControlRaw_getErrorLimitRaw(swigCPtr, this, j, SWIGTYPE_p_double.getCPtr(limit));
  }

  public boolean getErrorLimitsRaw(SWIGTYPE_p_double limits) {
    return yarpJNI.IPidControlRaw_getErrorLimitsRaw(swigCPtr, this, SWIGTYPE_p_double.getCPtr(limits));
  }

  public boolean resetPidRaw(int j) {
    return yarpJNI.IPidControlRaw_resetPidRaw(swigCPtr, this, j);
  }

  public boolean disablePidRaw(int j) {
    return yarpJNI.IPidControlRaw_disablePidRaw(swigCPtr, this, j);
  }

  public boolean enablePidRaw(int j) {
    return yarpJNI.IPidControlRaw_enablePidRaw(swigCPtr, this, j);
  }

  public boolean setOffsetRaw(int j, double v) {
    return yarpJNI.IPidControlRaw_setOffsetRaw(swigCPtr, this, j, v);
  }

}
