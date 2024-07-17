/**
 * Title: MyJavaTools: Client HTTP Request class
 *
 * <p>Description: this class helps to send POST HTTP requests with various form data, including
 * files. Cookies can be added to be included in the request.
 *
 * <p>Copyright: This is public domain; The right of people to use, distribute, copy or improve the
 * contents of the following may not be restricted.
 *
 * @author Vlad Patryshev, Alexei Trebounskikh
 * @version $Id$
 */
package org.jhotdraw.net;
import java.io.IOException;
public class ClientHttpRequest {
    java.net.URLConnection connection;

    java.io.OutputStream os = null;

    java.util.Map<java.lang.String, java.lang.String> cookies = new java.util.HashMap<>();

    java.lang.String rawCookies = "";

    protected void connect() throws java.io.IOException {
        if (os == null) {
            os = connection.getOutputStream();
        }
    }

    protected void write(char c) throws java.io.IOException {
        connect();
        os.write(c);
    }

    protected void write(java.lang.String s) throws java.io.IOException {
        connect();
        // BEGIN PATCH W. Randelshofer 2008-05-23 use UTF-8
        os.write(s.getBytes("UTF-8"));
        // END PATCH W. Randelshofer 2008-05-23 use UTF-8
    }

    protected void newline() throws java.io.IOException {
        connect();
        write("\r\n");
    }

    protected void writeln(java.lang.String s) throws java.io.IOException {
        connect();
        write(s);
        newline();
    }

    private static java.util.Random random = new java.util.Random();

    protected static java.lang.String randomString() {
        return java.lang.Long.toString(org.jhotdraw.net.ClientHttpRequest.random.nextLong(), 36);
    }

    java.lang.String boundary = (("---------------------------" + org.jhotdraw.net.ClientHttpRequest.randomString()) + org.jhotdraw.net.ClientHttpRequest.randomString()) + org.jhotdraw.net.ClientHttpRequest.randomString();

    private void boundary() throws java.io.IOException {
        write("--");
        write(boundary);
    }

    /**
     * Creates a new multipart POST HTTP request on a freshly opened URLConnection
     *
     * @param connection
     * 		an already open URL connection
     * @throws IOException
     */
    public ClientHttpRequest(java.net.URLConnection connection) throws java.io.IOException {
        this.connection = connection;
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    /**
     * Creates a new multipart POST HTTP request for a specified URL
     *
     * @param url
     * 		the URL to send request to
     * @throws IOException
     */
    public ClientHttpRequest(java.net.URL url) throws java.io.IOException {
        this(url.openConnection());
    }

    /**
     * Creates a new multipart POST HTTP request for a specified URL string
     *
     * @param urlString
     * 		the string representation of the URL to send request to
     * @throws IOException
     */
    public ClientHttpRequest(java.lang.String urlString) throws java.io.IOException {
        this(new java.net.URL(urlString));
    }

    private void postCookies() {
        java.lang.StringBuffer cookieList = new java.lang.StringBuffer(rawCookies);
        for (java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.String>> i = cookies.entrySet().iterator(); i.hasNext();) {
            java.util.Map.Entry<java.lang.String, java.lang.String> entry = i.next();
            cookieList.append((entry.getKey() + "=") + entry.getValue());
            if (i.hasNext()) {
                cookieList.append("; ");
            }
        }
        if (cookieList.length() > 0) {
            connection.setRequestProperty("Cookie", cookieList.toString());
        }
    }

    /**
     * adds a cookie to the requst
     *
     * @param rawCookies
     * 		A string with raw cookie data.
     * @throws IOException
     */
    public void setCookies(java.lang.String rawCookies) throws java.io.IOException {
        this.rawCookies = (rawCookies == null) ? "" : rawCookies;
        cookies.clear();
    }

    /**
     * adds a cookie to the requst
     *
     * @param name
     * 		cookie name
     * @param value
     * 		cookie value
     * @throws IOException
     */
    public void setCookie(java.lang.String name, java.lang.String value) throws java.io.IOException {
        cookies.put(name, value);
    }

    /**
     * adds cookies to the request
     *
     * @param cookies
     * 		the cookie "name-to-value" map
     * @throws IOException
     */
    public void setCookies(java.util.Map<java.lang.String, java.lang.String> cookies) throws java.io.IOException {
        if (cookies == null) {
            return;
        }
        this.cookies.putAll(cookies);
    }

    /**
     * adds cookies to the request
     *
     * @param cookies
     * 		array of cookie names and values (cookies[2*i] is a name, cookies[2*i + 1] is a
     * 		value)
     * @throws IOException
     */
    public void setCookies(java.lang.String[] cookies) throws java.io.IOException {
        if (cookies == null) {
            return;
        }
        for (int i = 0; i < (cookies.length - 1); i += 2) {
            setCookie(cookies[i], cookies[i + 1]);
        }
    }

    private void writeName(java.lang.String name) throws java.io.IOException {
        newline();
        write("Content-Disposition: form-data; name=\"");
        write(name);
        write('"');
    }

    /**
     * adds a string parameter to the request
     *
     * @param name
     * 		parameter name
     * @param value
     * 		parameter value
     * @throws IOException
     */
    public void setParameter(java.lang.String name, java.lang.String value) throws java.io.IOException {
        if (name == null) {
            throw new java.security.InvalidParameterException(((("setParameter(" + name) + ",") + value) + ") name must not be null");
        }
        if (value == null) {
            throw new java.security.InvalidParameterException(((("setParameter(" + name) + ",") + value) + ") value must not be null");
        }
        boundary();
        writeName(name);
        newline();
        newline();
        writeln(value);
    }

    private static void pipe(java.io.InputStream in, java.io.OutputStream out) throws java.io.IOException {
        byte[] buf = new byte[500000];
        int nread;
        int total = 0;
        synchronized(in) {
            while ((nread = in.read(buf, 0, buf.length)) >= 0) {
                out.write(buf, 0, nread);
                total += nread;
            } 
        }
        out.flush();
        buf = null;
    }

    /**
     * adds a file parameter to the request
     *
     * @param name
     * 		parameter name
     * @param filename
     * 		the name of the file
     * @param is
     * 		input stream to read the contents of the file from
     * @throws IOException
     */
    public void setParameter(java.lang.String name, java.lang.String filename, java.io.InputStream is) throws java.io.IOException {
        boundary();
        writeName(name);
        write("; filename=\"");
        write(filename);
        write('"');
        newline();
        write("Content-Type: ");
        java.lang.String type = java.net.URLConnection.guessContentTypeFromName(filename);
        if (type == null) {
            type = "application/octet-stream";
        }
        writeln(type);
        newline();
        org.jhotdraw.net.ClientHttpRequest.pipe(is, os);
        newline();
    }

    /**
     * adds a file parameter to the request
     *
     * @param name
     * 		parameter name
     * @param file
     * 		the file to upload
     * @throws IOException
     */
    public void setParameter(java.lang.String name, java.io.File file) throws java.io.IOException {
        try (java.io.FileInputStream in = new java.io.FileInputStream(file)) {
            setParameter(name, file.getPath(), in);
        }
    }

    /**
     * adds a parameter to the request; if the parameter is a File, the file is uploaded, otherwise
     * the string value of the parameter is passed in the request
     *
     * @param name
     * 		parameter name
     * @param object
     * 		parameter value, a File or anything else that can be stringified
     * @throws IOException
     */
    public void setParameter(java.lang.String name, java.lang.Object object) throws java.io.IOException {
        if (object instanceof java.io.File) {
            setParameter(name, ((java.io.File) (object)));
        } else {
            setParameter(name, object.toString());
        }
    }

    /**
     * adds parameters to the request
     *
     * @param parameters
     * 		"name-to-value" map of parameters; if a value is a file, the file is
     * 		uploaded, otherwise it is stringified and sent in the request
     * @throws IOException
     */
    public void setParameters(java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException {
        if (parameters != null) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : parameters.entrySet()) {
                setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * adds parameters to the request
     *
     * @param parameters
     * 		array of parameter names and values (parameters[2*i] is a name,
     * 		parameters[2*i + 1] is a value); if a value is a file, the file is uploaded, otherwise it
     * 		is stringified and sent in the request
     * @throws IOException
     */
    public void setParameters(java.lang.Object[] parameters) throws java.io.IOException {
        if (parameters != null) {
            for (int i = 0; i < (parameters.length - 1); i += 2) {
                setParameter(parameters[i].toString(), parameters[i + 1]);
            }
        }
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added
     *
     * @return input stream with the server response
     * @throws IOException
     */
    private java.io.InputStream doPost() throws java.io.IOException {
        boundary();
        writeln("--");
        os.close();
        return connection.getInputStream();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added
     *
     * @return input stream with the server response
     * @throws IOException
     */
    public java.io.InputStream post() throws java.io.IOException {
        postCookies();
        return doPost();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before
     * (if any), and with parameters that are passed in the argument
     *
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameters
     */
    public java.io.InputStream post(java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException {
        postCookies();
        setParameters(parameters);
        return doPost();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before
     * (if any), and with parameters that are passed in the argument
     *
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameters
     */
    public java.io.InputStream post(java.lang.Object[] parameters) throws java.io.IOException {
        postCookies();
        setParameters(parameters);
        return doPost();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before
     * (if any), and with cookies and parameters that are passed in the arguments
     *
     * @param cookies
     * 		request cookies
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameters
     * @see #setCookies
     */
    public java.io.InputStream post(java.util.Map<java.lang.String, java.lang.String> cookies, java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException {
        setCookies(cookies);
        postCookies();
        setParameters(parameters);
        return doPost();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before
     * (if any), and with cookies and parameters that are passed in the arguments
     *
     * @param raw_cookies
     * 		request cookies
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameters
     * @see #setCookies
     */
    public java.io.InputStream post(java.lang.String raw_cookies, java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException {
        setCookies(raw_cookies);
        postCookies();
        setParameters(parameters);
        return doPost();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before
     * (if any), and with cookies and parameters that are passed in the arguments
     *
     * @param cookies
     * 		request cookies
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameters
     * @see #setCookies
     */
    public java.io.InputStream post(java.lang.String[] cookies, java.lang.Object[] parameters) throws java.io.IOException {
        setCookies(cookies);
        postCookies();
        setParameters(parameters);
        return doPost();
    }

    /**
     * post the POST request to the server, with the specified parameter
     *
     * @param name
     * 		parameter name
     * @param value
     * 		parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public java.io.InputStream post(java.lang.String name, java.lang.Object value) throws java.io.IOException {
        postCookies();
        setParameter(name, value);
        return doPost();
    }

    /**
     * post the POST request to the server, with the specified parameters
     *
     * @param name1
     * 		first parameter name
     * @param value1
     * 		first parameter value
     * @param name2
     * 		second parameter name
     * @param value2
     * 		second parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public java.io.InputStream post(java.lang.String name1, java.lang.Object value1, java.lang.String name2, java.lang.Object value2) throws java.io.IOException {
        postCookies();
        setParameter(name1, value1);
        setParameter(name2, value2);
        return doPost();
    }

    /**
     * post the POST request to the server, with the specified parameters
     *
     * @param name1
     * 		first parameter name
     * @param value1
     * 		first parameter value
     * @param name2
     * 		second parameter name
     * @param value2
     * 		second parameter value
     * @param name3
     * 		third parameter name
     * @param value3
     * 		third parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public java.io.InputStream post(java.lang.String name1, java.lang.Object value1, java.lang.String name2, java.lang.Object value2, java.lang.String name3, java.lang.Object value3) throws java.io.IOException {
        postCookies();
        setParameter(name1, value1);
        setParameter(name2, value2);
        setParameter(name3, value3);
        return doPost();
    }

    /**
     * post the POST request to the server, with the specified parameters
     *
     * @param name1
     * 		first parameter name
     * @param value1
     * 		first parameter value
     * @param name2
     * 		second parameter name
     * @param value2
     * 		second parameter value
     * @param name3
     * 		third parameter name
     * @param value3
     * 		third parameter value
     * @param name4
     * 		fourth parameter name
     * @param value4
     * 		fourth parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public java.io.InputStream post(java.lang.String name1, java.lang.Object value1, java.lang.String name2, java.lang.Object value2, java.lang.String name3, java.lang.Object value3, java.lang.String name4, java.lang.Object value4) throws java.io.IOException {
        postCookies();
        setParameter(name1, value1);
        setParameter(name2, value2);
        setParameter(name3, value3);
        setParameter(name4, value4);
        return doPost();
    }

    /**
     * posts a new request to specified URL, with parameters that are passed in the argument
     *
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameters
     */
    public static java.io.InputStream post(java.net.URL url, java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(parameters);
    }

    /**
     * posts a new request to specified URL, with parameters that are passed in the argument
     *
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameters
     */
    public static java.io.InputStream post(java.net.URL url, java.lang.Object[] parameters) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(parameters);
    }

    /**
     * posts a new request to specified URL, with cookies and parameters that are passed in the
     * argument
     *
     * @param cookies
     * 		request cookies
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setCookies
     * @see #setParameters
     */
    public static java.io.InputStream post(java.net.URL url, java.util.Map<java.lang.String, java.lang.String> cookies, java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(cookies, parameters);
    }

    /**
     * posts a new request to specified URL, with cookies and parameters that are passed in the
     * argument
     *
     * @param url
     * 		post URL
     * @param cookies
     * 		request cookies
     * @param parameters
     * 		request parameters
     * @return input stream with the server response
     * @throws IOException
     * @see #setCookies
     * @see #setParameters
     */
    public static java.io.InputStream post(java.net.URL url, java.lang.String[] cookies, java.lang.Object[] parameters) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(cookies, parameters);
    }

    /**
     * post the POST request specified URL, with the specified parameter
     *
     * @param url
     * 		post URL
     * @param name1
     * 		parameter name
     * @param value1
     * 		parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public static java.io.InputStream post(java.net.URL url, java.lang.String name1, java.lang.Object value1) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(name1, value1);
    }

    /**
     * post the POST request to specified URL, with the specified parameters
     *
     * @param name1
     * 		first parameter name
     * @param value1
     * 		first parameter value
     * @param name2
     * 		second parameter name
     * @param value2
     * 		second parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public static java.io.InputStream post(java.net.URL url, java.lang.String name1, java.lang.Object value1, java.lang.String name2, java.lang.Object value2) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(name1, value1, name2, value2);
    }

    /**
     * post the POST request to specified URL, with the specified parameters
     *
     * @param name1
     * 		first parameter name
     * @param value1
     * 		first parameter value
     * @param name2
     * 		second parameter name
     * @param value2
     * 		second parameter value
     * @param name3
     * 		third parameter name
     * @param value3
     * 		third parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public static java.io.InputStream post(java.net.URL url, java.lang.String name1, java.lang.Object value1, java.lang.String name2, java.lang.Object value2, java.lang.String name3, java.lang.Object value3) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(name1, value1, name2, value2, name3, value3);
    }

    /**
     * post the POST request to specified URL, with the specified parameters
     *
     * @param name1
     * 		first parameter name
     * @param value1
     * 		first parameter value
     * @param name2
     * 		second parameter name
     * @param value2
     * 		second parameter value
     * @param name3
     * 		third parameter name
     * @param value3
     * 		third parameter value
     * @param name4
     * 		fourth parameter name
     * @param value4
     * 		fourth parameter value
     * @return input stream with the server response
     * @throws IOException
     * @see #setParameter
     */
    public static java.io.InputStream post(java.net.URL url, java.lang.String name1, java.lang.Object value1, java.lang.String name2, java.lang.Object value2, java.lang.String name3, java.lang.Object value3, java.lang.String name4, java.lang.Object value4) throws java.io.IOException {
        return new org.jhotdraw.net.ClientHttpRequest(url).post(name1, value1, name2, value2, name3, value3, name4, value4);
    }
}