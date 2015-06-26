/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class TypedReaderCallbackImageRgbFloat {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected TypedReaderCallbackImageRgbFloat(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TypedReaderCallbackImageRgbFloat obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_TypedReaderCallbackImageRgbFloat(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void onRead(ImageRgbFloat datum) {
    yarpJNI.TypedReaderCallbackImageRgbFloat_onRead__SWIG_0(swigCPtr, this, ImageRgbFloat.getCPtr(datum), datum);
  }

  public void onRead(ImageRgbFloat datum, TypedReaderImageRgbFloat reader) {
    yarpJNI.TypedReaderCallbackImageRgbFloat_onRead__SWIG_1(swigCPtr, this, ImageRgbFloat.getCPtr(datum), datum, TypedReaderImageRgbFloat.getCPtr(reader), reader);
  }

  public TypedReaderCallbackImageRgbFloat() {
    this(yarpJNI.new_TypedReaderCallbackImageRgbFloat(), true);
  }

}
