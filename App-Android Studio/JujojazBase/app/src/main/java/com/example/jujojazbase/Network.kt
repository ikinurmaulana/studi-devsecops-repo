package com.example.jujojazbase;
import kotlinx.coroutines.delay
import java.io.*
import java.net.HttpURLConnection
import java.net.Socket;
import java.net.URL

open class Network<ResultType>{
    var lib = Lib();
    var dest = "127.0.0.1";
    var port = 80;

    open fun onDone(result: List<Byte>){

    }
    
    open fun onDone(){

    }
    open fun onError(msg: String){

    }
    open fun onLoading(){

    }

    var resultData:ResultType? = null as ResultType;
    var error: String = "";
    var errorSig: Boolean = false;

    
    inner class NetSend(var dest:String, var port: Int, var buffer: List<Byte>, var delayMills: Long=0) : android.os.AsyncTask<String, String, List<Byte>>() {
        var sock: Socket? = null;

        override fun onPreExecute() {
            super.onPreExecute()
            onLoading();
        }

        override fun doInBackground(vararg p0: String?): List<Byte> {

            try {
                this.sock = Socket(dest, port);
                var outputStream:OutputStream;
                var inputStream:InputStream;
                outputStream = this.sock!!.getOutputStream();
                inputStream =  this.sock!!.getInputStream();
                outputStream.write(buffer.toByteArray());
                return inputStream.readBytes().toList();
                errorSig = false;
            }catch(e: Exception){
                var msg: String = "Error while connecting to server: " + e.toString();
                errorSig = true;
                error = msg;
                onError(msg);
                return listOf<Byte>();
            }
        }

        override fun onPostExecute(result: List<Byte>) {
            super.onPostExecute(result)
            if (errorSig){
                onError(error);
            }else{
                resultData = result as? ResultType;
                onDone();
                onDone(result);
            }
        }
    }
    inner class NetSendUrl(): android.os.AsyncTask<String, String, List<Byte>>(){
        var buffer: Array<Byte>? = null;
        var url: String? = null;
        var delay:Long? = null
        constructor(url: String, buffer: Array<Byte>, delayMills: Long=0) : this() {
            this.buffer = buffer;
            this.url = url;
            this.delay = delayMills;
        }
        override fun onPreExecute() {
            super.onPreExecute()
            onLoading();
        }
        
        override fun doInBackground(vararg p0: String?): List<Byte> {
            var url:URL;
            var connection: HttpURLConnection;
            try{
                System.out.println("doInBackground() called")
                url = URL(this.url);
                System.out.println("url object created")
                connection = url.openConnection() as HttpURLConnection;
                System.out.println("url object created")
                connection.requestMethod = "POST";
//                var temp = byteArrayOf();

                System.out.println("Data send: " + String(buffer!!.toByteArray()));


                connection.outputStream.write(buffer!!.toByteArray())
//                this.buffer = temp.toList();
                var result = connection.inputStream.readBytes().toList();
                connection.disconnect();
                errorSig = false;
                return result;
            }catch (e: Exception){
                onError("Connection fail: "+e.toString());
                errorSig = true;
                return listOf<Byte>();
            }
        }

        override fun onPostExecute(result: List<Byte>) {
            super.onPostExecute(result)
            Thread.sleep(delay!!);
            if (errorSig){
                onError(error);
            }else{
                onDone();
                onDone(result);
            }

        }
    }

    inner class SimulateSlow(var delayMills: Integer) : android.os.AsyncTask<String, String, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            onLoading();
        }

        override fun doInBackground(vararg p0: String?): Boolean {
            Thread.sleep(delayMills as Long);
            return true;
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            onDone();
        }
    }

    fun simulateSlow(delayMills: Integer){
        SimulateSlow(delayMills).execute();
    }


    fun send(dest:String, port: Int, buffer: List<Byte>, delayMills: Long=0): android.os.AsyncTask<String, String, List<Byte>>{
        this.dest = dest;
        this.port = port;
        var net: android.os.AsyncTask<String, String, List<Byte>> = NetSend(dest, port, buffer, delayMills);
        net.execute();
        return net;
    }
    fun send(dest:String, buffer: List<Byte>, delayMills: Long): android.os.AsyncTask<String, String, List<Byte>>{
        return this.send(dest, port, buffer, delayMills);
    }
    fun sendUrl(url: String, buffer: Array<Byte>, delay: Long): android.os.AsyncTask<String, String, List<Byte>>{
        System.out.println("sendUrl(s, ba, d) called")
        var net: android.os.AsyncTask<String, String, List<Byte>> = NetSendUrl(url, buffer, delay);
        net.execute();
        return net;
    }
}