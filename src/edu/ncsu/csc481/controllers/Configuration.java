package edu.ncsu.csc481.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;

public class Configuration {
	private File file;
	private FileReader fileReader;
	private BufferedReader reader; 
	
	private Gson gson;
	private DataContainer data;
	
	public Configuration(String fileName) {
		gson = new Gson();
		
		try {
			file = new File(fileName);
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readConfiguration() {
		data = gson.fromJson(reader, DataContainer.class);
	}
	
	public DataContainer getData() {
		return data;
	}
	
	public static class Excluder implements ExclusionStrategy {
		private Class<?> type;
		
		public Excluder(Class<?> type) {
			this.type = type;
		}
		
		public boolean shouldSkipClass(Class<?> c) {
			return (type == c);
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return false;
		}
	}
	
	public class DataContainer {
		private List<Data> objects;
		
		public void setData(List<Data> data) {
			this.objects = data;
		}
		
		public List<Data> getData() {
			return objects;
		}
		
		public String toString() {
			String string = "";
			
			for(Data d : objects) {
				string += d.toString() + "\n";
			}
			
			return string;
		}
	}
	
	public class Data {
		private String id;
		private String type;
		private List<Integer> position;
		private List<Integer> dimensions;
		
		public void setId(String id) {
			this.id = id;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
		public void setPosition(List<Integer> position) {
			this.position = position;
		}
		
		public void setDomensions(List<Integer> dimensions) {
			this.dimensions = dimensions;
		}
		
		public String getId() {
			return id;
		}
		
		public String getType() {
			return type;
		}
		
		public List<Integer> getPosition() {
			return position;
		}
		
		public List<Integer> getDimensions() {
			return dimensions;
		}
		
		public String toString() {
			return String.format("id: %s, type: %s, position: %s, dimensions: %s", id, type, position, dimensions);
		}
	}
	
	public class JsonClass {
		private String id;
		private int radius;
		private float x;
		private float y;
		
		public String getId() {
			return id;
		}
		
		public int getRadius() {
			return radius;
		}
		
		public float getX() {
			return x;
		}
		
		public float getY() {
			return y;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public void setRadius(int radius) {
			this.radius = radius;
		}
		
		public void setX(float x) {
			this.x = x;
		}
		
		public void setY(float y) {
			this.y = y;
		}
		
		public String toString() {
			return String.format("id: %s, radius: %d, x: %f, y: %f", id, radius, x, y);
		}
	}
}
