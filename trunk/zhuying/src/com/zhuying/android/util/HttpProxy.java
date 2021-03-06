package com.zhuying.android.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HttpProxy
{

    private String url;
    private String method;
    private Map headers;
    private byte body[];
    private String resEncode;
    private int respCode;
    private byte respData[];
    private int connetTimeout;
	private int readTimeout;
/**
 * HttpProxy proxy = new HttpProxy("http://www.baidu.com");
        proxy.exec();
        System.out.println(proxy.getResponseCode());
 * @param url
 */
    public HttpProxy(String url)
    {
        this.url = url;
        headers = new HashMap();
        connetTimeout = 50000;
        readTimeout = 6000000;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public void setHeader(String name, String value)
    {
        headers.put(name, value);
    }

    public void setAccept(String accept)
    {
        setHeader("Accept", accept);
    }

    public void setUserAgent(String userAgent)
    {
        setHeader("User-Agent", userAgent);
    }

    public void setHost(String host)
    {
        setHeader("Host", host);
    }

    public void setBody(byte body[])
    {
        this.body = body;
    }

    public void setBody(String body)
    {
        try
        {
            setBody(body, null);
        }
        catch(Exception exception) { }
    }

    public void setBody(String body, String encode)throws UnsupportedEncodingException
    {
        if(encode != null && !encode.equals(""))
            this.body = body.getBytes(encode);
        else
            this.body = body.getBytes();
    }

    public String getContentEncoding()
    {
        return resEncode;
    }

    public int getContentLength()
    {
        return respData.length;
    }

    public int getResponseCode()
    {
        return respCode;
    }

    public byte[] getResponseData()
    {
        return respData;
    }

    public String getResponseString()
    {
        if(respData == null)
            return null;
        String ret = null;
        if(resEncode == null)
            ret = new String(respData);
        else
            try
            {
                ret = new String(respData, resEncode);
            }
            catch(Exception exception) { }
        return ret;
    }

    public void exec() throws IOException
    {
        HttpURLConnection conn=null;
        OutputStream os=null;
        InputStream is=null;
        ByteArrayOutputStream baos=null;
        try
        {
            URL urlObject = new URL(url);
            conn = (HttpURLConnection)urlObject.openConnection();
            conn.setConnectTimeout(connetTimeout);
            conn.setReadTimeout(readTimeout);
            if(method != null)
                conn.setRequestMethod(method);
            java.util.Map.Entry entry;
            for(Iterator iterator = headers.entrySet().iterator();iterator.hasNext();){
            	entry = (java.util.Map.Entry)iterator.next();
                conn.setRequestProperty((String)entry.getKey(),(String)entry.getValue());
            }
            

            if(body != null)
            {
                conn.setDoOutput(true);
                os = conn.getOutputStream();
                os.write(body);
                os.flush();
            }
            respCode = conn.getResponseCode();
            resEncode = conn.getContentEncoding();
            is = conn.getInputStream();
            baos = new ByteArrayOutputStream(2048);
            byte buf[] = new byte[2048];
            for(int len = 0; (len = is.read(buf)) != -1;)
                baos.write(buf, 0, len);

            respData = baos.toByteArray();
        }
        catch(IOException ie)
        {
        	ie.printStackTrace();
            throw ie;
        }finally {
        	if(baos!=null)
        		baos.close();
        	if(os!=null)
        		os.close();
        	if(is!=null)
        		is.close();
        	if(conn!=null)
        		conn.disconnect();
        }

    }
    public int getConnetTimeout() {
		return connetTimeout;
	}

	public void setConnetTimeout(int connetTimeout) {
		this.connetTimeout = connetTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
    public static void main(String[] args) {
		System.out.println("jkashfksahfdksahfdkasdhfksajhfksajhfksaf");

	}
}
