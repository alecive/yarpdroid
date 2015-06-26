/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class IFrameWriterAudioVisual {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IFrameWriterAudioVisual(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IFrameWriterAudioVisual obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_IFrameWriterAudioVisual(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean putAudioVisual(ImageRgb image, Sound sound) {
    return yarpJNI.IFrameWriterAudioVisual_putAudioVisual(swigCPtr, this, ImageRgb.getCPtr(image), image, Sound.getCPtr(sound), sound);
  }

}
