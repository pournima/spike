package com.checkinlibrary.facebook.android;

import java.util.List;

public class FBPermissionsResult {
  
	List<Data> data=null;
	
	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data{
		int installed;
		int read_stream;
		int email;
		int publish_stream;
		
		public int getInstalled() {
			return installed;
		}
		public void setInstalled(int installed) {
			this.installed = installed;
		}
		public int getRead_stream() {
			return read_stream;
		}
		public void setRead_stream(int read_stream) {
			this.read_stream = read_stream;
		}
		public int getEmail() {
			return email;
		}
		public void setEmail(int email) {
			this.email = email;
		}
		public int getPublish_stream() {
			return publish_stream;
		}
		public void setPublish_stream(int publish_stream) {
			this.publish_stream = publish_stream;
		}	
	}
}
