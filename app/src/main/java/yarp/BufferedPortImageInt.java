/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package yarp;

public class BufferedPortImageInt extends Contactable {
  private long swigCPtr;

  protected BufferedPortImageInt(long cPtr, boolean cMemoryOwn) {
    super(yarpJNI.BufferedPortImageInt_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(BufferedPortImageInt obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        yarpJNI.delete_BufferedPortImageInt(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public BufferedPortImageInt() {
    this(yarpJNI.new_BufferedPortImageInt__SWIG_0(), true);
  }

  public BufferedPortImageInt(Port port) {
    this(yarpJNI.new_BufferedPortImageInt__SWIG_1(Port.getCPtr(port), port), true);
  }

  public boolean addOutput(String name) {
    return yarpJNI.BufferedPortImageInt_addOutput__SWIG_0(swigCPtr, this, name);
  }

  public boolean addOutput(String name, String carrier) {
    return yarpJNI.BufferedPortImageInt_addOutput__SWIG_1(swigCPtr, this, name, carrier);
  }

  public boolean addOutput(Contact contact) {
    return yarpJNI.BufferedPortImageInt_addOutput__SWIG_2(swigCPtr, this, Contact.getCPtr(contact), contact);
  }

  public void close() {
    yarpJNI.BufferedPortImageInt_close(swigCPtr, this);
  }

  public void interrupt() {
    yarpJNI.BufferedPortImageInt_interrupt(swigCPtr, this);
  }

  public void resume() {
    yarpJNI.BufferedPortImageInt_resume(swigCPtr, this);
  }

  public int getPendingReads() {
    return yarpJNI.BufferedPortImageInt_getPendingReads(swigCPtr, this);
  }

  public Contact where() {
    return new Contact(yarpJNI.BufferedPortImageInt_where(swigCPtr, this), true);
  }

  public String getName() {
    return yarpJNI.BufferedPortImageInt_getName(swigCPtr, this);
  }

  public ImageInt prepare() {
    return new ImageInt(yarpJNI.BufferedPortImageInt_prepare(swigCPtr, this), false);
  }

  public boolean unprepare() {
    return yarpJNI.BufferedPortImageInt_unprepare(swigCPtr, this);
  }

  public void write(boolean forceStrict) {
    yarpJNI.BufferedPortImageInt_write__SWIG_0(swigCPtr, this, forceStrict);
  }

  public void write() {
    yarpJNI.BufferedPortImageInt_write__SWIG_1(swigCPtr, this);
  }

  public void writeStrict() {
    yarpJNI.BufferedPortImageInt_writeStrict(swigCPtr, this);
  }

  public void waitForWrite() {
    yarpJNI.BufferedPortImageInt_waitForWrite(swigCPtr, this);
  }

  public void setStrict(boolean strict) {
    yarpJNI.BufferedPortImageInt_setStrict__SWIG_0(swigCPtr, this, strict);
  }

  public void setStrict() {
    yarpJNI.BufferedPortImageInt_setStrict__SWIG_1(swigCPtr, this);
  }

  public ImageInt read(boolean shouldWait) {
    long cPtr = yarpJNI.BufferedPortImageInt_read__SWIG_0(swigCPtr, this, shouldWait);
    return (cPtr == 0) ? null : new ImageInt(cPtr, false);
  }

  public ImageInt read() {
    long cPtr = yarpJNI.BufferedPortImageInt_read__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new ImageInt(cPtr, false);
  }

  public ImageInt lastRead() {
    long cPtr = yarpJNI.BufferedPortImageInt_lastRead(swigCPtr, this);
    return (cPtr == 0) ? null : new ImageInt(cPtr, false);
  }

  public boolean isClosed() {
    return yarpJNI.BufferedPortImageInt_isClosed(swigCPtr, this);
  }

  public void setReplier(PortReader reader) {
    yarpJNI.BufferedPortImageInt_setReplier(swigCPtr, this, PortReader.getCPtr(reader), reader);
  }

  public void setReader(PortReader reader) {
    yarpJNI.BufferedPortImageInt_setReader(swigCPtr, this, PortReader.getCPtr(reader), reader);
  }

  public void setAdminReader(PortReader reader) {
    yarpJNI.BufferedPortImageInt_setAdminReader(swigCPtr, this, PortReader.getCPtr(reader), reader);
  }

  public void onRead(ImageInt datum) {
    yarpJNI.BufferedPortImageInt_onRead(swigCPtr, this, ImageInt.getCPtr(datum), datum);
  }

  public void useCallback(TypedReaderCallbackImageInt callback) {
    yarpJNI.BufferedPortImageInt_useCallback__SWIG_0(swigCPtr, this, TypedReaderCallbackImageInt.getCPtr(callback), callback);
  }

  public void useCallback() {
    yarpJNI.BufferedPortImageInt_useCallback__SWIG_1(swigCPtr, this);
  }

  public void disableCallback() {
    yarpJNI.BufferedPortImageInt_disableCallback(swigCPtr, this);
  }

  public boolean setEnvelope(PortWriter envelope) {
    return yarpJNI.BufferedPortImageInt_setEnvelope(swigCPtr, this, PortWriter.getCPtr(envelope), envelope);
  }

  public boolean getEnvelope(PortReader envelope) {
    return yarpJNI.BufferedPortImageInt_getEnvelope(swigCPtr, this, PortReader.getCPtr(envelope), envelope);
  }

  public int getInputCount() {
    return yarpJNI.BufferedPortImageInt_getInputCount(swigCPtr, this);
  }

  public int getOutputCount() {
    return yarpJNI.BufferedPortImageInt_getOutputCount(swigCPtr, this);
  }

  public boolean isWriting() {
    return yarpJNI.BufferedPortImageInt_isWriting(swigCPtr, this);
  }

  public void getReport(PortReport reporter) {
    yarpJNI.BufferedPortImageInt_getReport(swigCPtr, this, PortReport.getCPtr(reporter), reporter);
  }

  public void setReporter(PortReport reporter) {
    yarpJNI.BufferedPortImageInt_setReporter(swigCPtr, this, PortReport.getCPtr(reporter), reporter);
  }

  public SWIGTYPE_p_void acquire() {
    long cPtr = yarpJNI.BufferedPortImageInt_acquire(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
  }

  public void release(SWIGTYPE_p_void handle) {
    yarpJNI.BufferedPortImageInt_release(swigCPtr, this, SWIGTYPE_p_void.getCPtr(handle));
  }

  public void setTargetPeriod(double period) {
    yarpJNI.BufferedPortImageInt_setTargetPeriod(swigCPtr, this, period);
  }

  public SWIGTYPE_p_Type getType() {
    return new SWIGTYPE_p_Type(yarpJNI.BufferedPortImageInt_getType(swigCPtr, this), true);
  }

  public void promiseType(SWIGTYPE_p_Type typ) {
    yarpJNI.BufferedPortImageInt_promiseType(swigCPtr, this, SWIGTYPE_p_Type.getCPtr(typ));
  }

  public void setInputMode(boolean expectInput) {
    yarpJNI.BufferedPortImageInt_setInputMode(swigCPtr, this, expectInput);
  }

  public void setOutputMode(boolean expectOutput) {
    yarpJNI.BufferedPortImageInt_setOutputMode(swigCPtr, this, expectOutput);
  }

  public void setRpcMode(boolean expectRpc) {
    yarpJNI.BufferedPortImageInt_setRpcMode(swigCPtr, this, expectRpc);
  }

  public Property acquireProperties(boolean readOnly) {
    long cPtr = yarpJNI.BufferedPortImageInt_acquireProperties(swigCPtr, this, readOnly);
    return (cPtr == 0) ? null : new Property(cPtr, false);
  }

  public void releaseProperties(Property prop) {
    yarpJNI.BufferedPortImageInt_releaseProperties(swigCPtr, this, Property.getCPtr(prop), prop);
  }

  public void includeNodeInName(boolean flag) {
    yarpJNI.BufferedPortImageInt_includeNodeInName(swigCPtr, this, flag);
  }

  public boolean setCallbackLock(SWIGTYPE_p_yarp__os__Mutex mutex) {
    return yarpJNI.BufferedPortImageInt_setCallbackLock(swigCPtr, this, SWIGTYPE_p_yarp__os__Mutex.getCPtr(mutex));
  }

  public boolean removeCallbackLock() {
    return yarpJNI.BufferedPortImageInt_removeCallbackLock(swigCPtr, this);
  }

  public boolean lockCallback() {
    return yarpJNI.BufferedPortImageInt_lockCallback(swigCPtr, this);
  }

  public boolean tryLockCallback() {
    return yarpJNI.BufferedPortImageInt_tryLockCallback(swigCPtr, this);
  }

  public void unlockCallback() {
    yarpJNI.BufferedPortImageInt_unlockCallback(swigCPtr, this);
  }

}
