package com.github.edgar615.spring.web.log;

import com.google.common.io.CharStreams;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ResettableStreamRequestWrapper extends HttpServletRequestWrapper {
  private final String body;

  public ResettableStreamRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    body = CharStreams.toString(new InputStreamReader(request.getInputStream()));
  }

  public String getRequestBody() {
    return body;
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream(body.getBytes());
    return new ResettableServletInputStream(in, super.getInputStream());
  }

  private static final class ResettableServletInputStream extends ServletInputStream {
    private final ServletInputStream inputStream;

    private ByteArrayInputStream bais;

    public ResettableServletInputStream(ByteArrayInputStream bais, ServletInputStream inputStream) {
      this.bais = bais;
      this.inputStream = inputStream;
    }

    @Override
    public int available() {
      return this.bais.available();
    }

    @Override
    public int read() {
      return this.bais.read();
    }

    @Override
    public int read(byte[] buf, int off, int len) {
      return this.bais.read(buf, off, len);
    }

    @Override
    public boolean isFinished() {
      return inputStream.isFinished();
    }

    @Override
    public boolean isReady() {
      return inputStream.isReady();
    }

    @Override
    public void setReadListener(ReadListener listener) {
      inputStream.setReadListener(listener);
    }
  }
}
