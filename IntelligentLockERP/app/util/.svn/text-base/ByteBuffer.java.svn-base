package util;

public class ByteBuffer {
  static public int INCREASE_SIZE = 10240;
  static byte GetNoCaseChar(byte code) {
    if (code >= (byte) 'a' && code <= (byte) 'z') {
      return (byte) (code - 'a' + 'A');
    }
    if (code >= (byte) 'A' && code <= (byte) 'Z') {
      return (byte) (code + 'a' - 'A');
    }
    return code;
  }

  byte[] m_sBuff;
  int m_sLength; //alloced size;
  int m_sSize; //current size;
  int m_nIncrease; //when need realloc
  int m_nOffset;

  //function
  public ByteBuffer() {
    m_nIncrease = INCREASE_SIZE;
  };

  public ByteBuffer(int nAlloc, int nIncrease) {
    m_sBuff = new byte[nAlloc];
    m_nIncrease = nIncrease;
    m_sLength = nAlloc;
    m_sSize = 0;
    m_nOffset = 0;
  };

  //retrive data;
  public byte[] getData() {
    return m_sBuff;
  }

  public void setData(byte[] sdata) {
    m_sBuff = sdata;
    m_sLength = sdata.length;
    m_sSize = m_sLength;
  }

  public int getLength() {
    return m_sLength;
  }

  public boolean ResetLength(int nLength) {
    byte[] sTemp;
    try {
      sTemp = new byte[nLength];
      int nCopy = nLength;
      if (nLength > m_sBuff.length)
        nCopy = m_sBuff.length;
      if (CopyBuffer(sTemp, m_sBuff, nCopy, 0, 0) == false)
        return false;

    }
    catch (Exception e) {
      return false;
    }
    m_sBuff = sTemp;
    m_sLength = nLength;
    if (m_sSize > m_sLength)
      m_sSize = m_sLength;
    return true;
  }

  public int getSize() {
    return m_sSize;
  }

  public void setSize(int nSize) {
    m_sSize = nSize;
    if (m_sSize > m_sLength)
      m_sSize = m_sLength;
  }

  public int getOffset() {
    return m_nOffset;
  }

  public void setOffset(int nOff) {
    m_nOffset = nOff;
    if (m_nOffset > m_sLength)
      m_nOffset = m_sLength;
    m_sSize = m_sLength - m_nOffset;
  }

  public void Offset(int nOff) {
    m_nOffset += nOff;
    setSize(m_sSize - nOff);
  }

  public void SetIncrease(int nIncrease) {
    m_nIncrease = nIncrease;
    if (m_nIncrease < 0)
      m_nIncrease = 0;
  }

  //append
  public boolean append(int n) {
    return append(Integer.toString(n));
  }

  public boolean append(String src, int nOffset, int nLength) {
    if (src == null)
      return false;
    if (nOffset + nLength > src.length())
      return false;
    return append(src.substring(nOffset, nOffset + nLength).getBytes());
  }

  public boolean append(String src) {
    if (src == null)
      return false;
    return append(src.getBytes());
  }

  public boolean append(byte[] sAdd) {
    if (sAdd == null)
      return false;
    return append(sAdd, 0, sAdd.length);
  }

  public boolean append(byte[] sAdd, int nOffset, int length) {
    if (sAdd == null)
      return false;
    if (sAdd == null)
      return false;
    if (nOffset + length > sAdd.length)
      return false;
    if (m_sLength - m_sSize < length && m_nIncrease <= 0)
      return false;

    int nAlloc = m_sLength + m_nIncrease;
    if (m_sLength - m_sSize < length) {
      if (nAlloc - m_sSize < length)
        nAlloc = m_sSize + length + m_nIncrease;
      if (ResetLength(nAlloc) == false)
        return false;
    }
    if (CopyBuffer(m_sBuff, sAdd, length, m_sSize, nOffset) == false)
      return false;
    m_sSize += length;
    return true;
  }

  //copy func.
  public static boolean CopyBuffer(byte[] Des, byte[] Src, int nLength,
                                   int nOffsetDes,
                                   int nOffsetSrc) {
    if (Src == null || Des == null || nLength < 0)
      return false;
    if (Src.length < nOffsetSrc + nLength || Des.length < nOffsetDes + nLength)
      return false;
    for (int i = 0; i < nLength; i++)
      Des[i + nOffsetDes] = Src[i + nOffsetSrc];
    return true;
  }

  public boolean CopyBuffer(byte[] Des, int nLength, int nOffsetDes,
                            int nOffsetSrc) {

    return CopyBuffer(Des, m_sBuff, nLength, nOffsetDes,
                      nOffsetSrc);
  }


}