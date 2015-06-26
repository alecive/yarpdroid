/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class PortReaderBufferBase extends PortReader {
  private long swigCPtr;

  protected PortReaderBufferBase(long cPtr, boolean cMemoryOwn) {
    super(yarpJNI.PortReaderBufferBase_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(PortReaderBufferBase obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_PortReaderBufferBase(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public PortReaderBufferBase(long maxBuffer) {
    this(yarpJNI.new_PortReaderBufferBase(maxBuffer), true);
  }

  public void setCreator(PortReaderBufferBaseCreator creator) {
    yarpJNI.PortReaderBufferBase_setCreator(swigCPtr, this, PortReaderBufferBaseCreator.getCPtr(creator), creator);
  }

  public void setReplier(PortReader reader) {
    yarpJNI.PortReaderBufferBase_setReplier(swigCPtr, this, PortReader.getCPtr(reader), reader);
  }

  public void setPrune(boolean flag) {
    yarpJNI.PortReaderBufferBase_setPrune__SWIG_0(swigCPtr, this, flag);
  }

  public void setPrune() {
    yarpJNI.PortReaderBufferBase_setPrune__SWIG_1(swigCPtr, this);
  }

  public void setAllowReuse(boolean flag) {
    yarpJNI.PortReaderBufferBase_setAllowReuse__SWIG_0(swigCPtr, this, flag);
  }

  public void setAllowReuse() {
    yarpJNI.PortReaderBufferBase_setAllowReuse__SWIG_1(swigCPtr, this);
  }

  public void setTargetPeriod(double period) {
    yarpJNI.PortReaderBufferBase_setTargetPeriod(swigCPtr, this, period);
  }

  public String getName() {
    return yarpJNI.PortReaderBufferBase_getName(swigCPtr, this);
  }

  public long getMaxBuffer() {
    return yarpJNI.PortReaderBufferBase_getMaxBuffer(swigCPtr, this);
  }

  public boolean isClosed() {
    return yarpJNI.PortReaderBufferBase_isClosed(swigCPtr, this);
  }

  public void clear() {
    yarpJNI.PortReaderBufferBase_clear(swigCPtr, this);
  }

  public PortReader create() {
    long cPtr = yarpJNI.PortReaderBufferBase_create(swigCPtr, this);
    return (cPtr == 0) ? null : new PortReader(cPtr, false);
  }

  public void release(PortReader completed) {
    yarpJNI.PortReaderBufferBase_release__SWIG_0(swigCPtr, this, PortReader.getCPtr(completed), completed);
  }

  public int check() {
    return yarpJNI.PortReaderBufferBase_check(swigCPtr, this);
  }

  public boolean read(ConnectionReader connection) {
    return yarpJNI.PortReaderBufferBase_read(swigCPtr, this, ConnectionReader.getCPtr(connection), connection);
  }

  public PortReader readBase(SWIGTYPE_p_bool missed, boolean cleanup) {
    long cPtr = yarpJNI.PortReaderBufferBase_readBase(swigCPtr, this, SWIGTYPE_p_bool.getCPtr(missed), cleanup);
    return (cPtr == 0) ? null : new PortReader(cPtr, false);
  }

  public void interrupt() {
    yarpJNI.PortReaderBufferBase_interrupt(swigCPtr, this);
  }

  public void attachBase(Port port) {
    yarpJNI.PortReaderBufferBase_attachBase(swigCPtr, this, Port.getCPtr(port), port);
  }

  public boolean acceptObjectBase(PortReader obj, PortWriter wrapper) {
    return yarpJNI.PortReaderBufferBase_acceptObjectBase(swigCPtr, this, PortReader.getCPtr(obj), obj, PortWriter.getCPtr(wrapper), wrapper);
  }

  public boolean forgetObjectBase(PortReader obj, PortWriter wrapper) {
    return yarpJNI.PortReaderBufferBase_forgetObjectBase(swigCPtr, this, PortReader.getCPtr(obj), obj, PortWriter.getCPtr(wrapper), wrapper);
  }

  public boolean getEnvelope(PortReader envelope) {
    return yarpJNI.PortReaderBufferBase_getEnvelope(swigCPtr, this, PortReader.getCPtr(envelope), envelope);
  }

  public SWIGTYPE_p_void acquire() {
    long cPtr = yarpJNI.PortReaderBufferBase_acquire(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
  }

  public void release(SWIGTYPE_p_void key) {
    yarpJNI.PortReaderBufferBase_release__SWIG_1(swigCPtr, this, SWIGTYPE_p_void.getCPtr(key));
  }

  public void setAutoRelease(boolean flag) {
    yarpJNI.PortReaderBufferBase_setAutoRelease__SWIG_0(swigCPtr, this, flag);
  }

  public void setAutoRelease() {
    yarpJNI.PortReaderBufferBase_setAutoRelease__SWIG_1(swigCPtr, this);
  }

}
