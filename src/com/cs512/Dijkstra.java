package com.cs512;

import java.io.IOException;
import java.util.*;
import java.sql.*;


public class Dijkstra {
	
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://cs512db.csab6py4gsye.us-east-1.rds.amazonaws.com:3306/CS512";
 
   
    static final String USER = "admincs512";
    static final String PASS = "Matkhau18!";
    
    public String printPath() throws IOException {
    	//extract data
    	ArrayList<Helper> HelperList=new ArrayList<Helper>();
        ArrayList<NeedHelp> NeedHelpList=new ArrayList<NeedHelp>();
        
    	Connection conn = null;
        Statement stmt = null;
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "select * from Requests";
            ResultSet rs = stmt.executeQuery(sql);
        
            while(rs.next()){
                
                
            	if(Objects.equals(rs.getString(1), "help"))
                {
                	NeedHelpList.add(new NeedHelp(rs.getString(2), rs.getString(3), rs.getString(5), rs.getString(4), rs.getString(6)));
                	
                	
                }
                else if(Objects.equals(rs.getString(1), "helper"))
                {
                	HelperList.add(new Helper(rs.getString(3), rs.getString(5), rs.getString(4), rs.getString(6)));
                	
                	
                }
                
            }
            
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            
            se.printStackTrace();
        }catch(Exception e){
            
            e.printStackTrace();
        }finally{
            
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
       
    	//end of extract
    	
    	
    	//Make graph
        Iterator itrNHL = NeedHelpList.iterator();
        Map<String, Integer> myMap = new HashMap<String, Integer>();
        
        int placeNumbers = 5;
        myMap.put("Livingston", 0);
        myMap.put("College Ave", 1);
        myMap.put("Douglass", 2);
        myMap.put("Busch", 3);
        myMap.put("Cook", 4);
        
        int[][] eta = new int [placeNumbers][placeNumbers];
        
        eta[0][1] = 15;
        eta[1][0] = 15;
        eta[1][3] = 20;
        eta[3][1] = 20;
        eta[1][2] = 15;
        eta[2][1] = 15;
        eta[1][4] = 20;
        eta[4][1] = 20;
        eta[0][3] = 10;
        eta[3][0] = 10;
        eta[0][2] = 25;
        eta[2][0] = 25;
        eta[0][4] = 30;
        eta[4][0] = 30;
        eta[2][3] = 20;
        eta[3][2] = 20;
        eta[3][4] = 25;
        eta[4][3] = 25;
        eta[2][4] = 10;
        eta[4][2] = 10;
        		
        
        
        //make graph end
       
        //Trying for each help request
        
        while(itrNHL.hasNext()){  
        	
        	int[] dist = new int[placeNumbers];
            int[] prev = new int[placeNumbers];
            boolean[] used = new boolean[placeNumbers];
            Helper[] who = new Helper[placeNumbers];
            
            for(int i = 0; i < placeNumbers; i++) {
            	dist[i] = 2147483647;
            	prev[i] = -1;
            	used[i] = false;
            }
        	//Dijkstra
            NeedHelp nh=(NeedHelp)itrNHL.next(); 
            
            int start = myMap.get(nh.FromLocation);
            int goal = myMap.get(nh.ToLocation);
            //System.out.println(start + " " + goal);
            int st = ttm(nh.FromTime); //start time
            int et = ttm(nh.ToTime); // end time
            dist[start] = 0;
            for(int j = 0;j < placeNumbers; j++)
            {
            	int now = -1;
            	int min = 2147483647;
            	for(int i = 0; i < placeNumbers; i++)
            	{
            		if(used[i])continue;
            		if(dist[i] < min)
            		{
            			now = i;
            			min = dist[i];
            		}
            	}
            	if (now == -1)break;
            	used[now] = true;
            	
            	//System.out.println(now);
            	
            	Iterator itr = HelperList.iterator();
            	while(itr.hasNext())
            	{
            		Helper h =(Helper)itr.next();
            		if (!Objects.equals(now,myMap.get(h.FromLocation)))continue;
            		int u = myMap.get(h.ToLocation);
            		if (st + dist[now] < ttm(h.StartTime))
            		{
            			
            			if (ttm(h.StartTime) - st + eta[now][u] < dist[u])
            			{
            				
            				dist[u] = ttm(h.StartTime) - st + eta[now][u];
            				//System.out.println(u + " " + dist[u]);
            				prev[u] = now;
            				who[u] = h;
            			}
            		}
            		else if (st + dist[now] < ttm(h.EndTime))
            		{
            			if(dist[now] + eta[now][u] < dist[u])
            			{
            				dist[u] = dist[now] + eta[now][u];
            				//System.out.println(u + " " + dist[u]);
            				prev[u] = now;
            				who[u] = h;
            			}
            		}
            		
            	}
            	
            }
            
            if(dist[goal] == 2147483647)
            {	
            	System.out.println("Sorry, no suitable helpers found.");
            	return "Sorry, no suitable helpers found.\n";
            	//continue;
            }
            int now = goal;
            ArrayList<Helper> HelperForThis=new ArrayList<Helper>();
            while(now != start)
            {
            	HelperForThis.add(who[now]);
            	now = prev[now];
            }
            int t = st;
            StringBuilder returnStr = new StringBuilder();
            for(int i = HelperForThis.size() - 1; i >=0; i--)
            {
            	
            	Helper h = HelperForThis.get(i);
            	System.out.println("Helper: " + h.FromLocation + " " + h.StartTime + " " + h.ToLocation+ " " + h.EndTime);
            	String tmp = "Helper: " + h.FromLocation + " " + h.StartTime + " " + h.ToLocation+ " " + h.EndTime;
            	returnStr.append(tmp);
            	returnStr.append("\n");
            	if (t < ttm(h.StartTime))
            	{
            		System.out.println("Wait at " + h.FromLocation + " till " + h.StartTime + ". ");
            		String tmp1 = "Wait at " + h.FromLocation + " till " + h.StartTime + ". ";
            		returnStr.append(tmp1);
            		returnStr.append("\n");
            		t = ttm(h.StartTime);
            	}
            	System.out.println("Deliver " + nh.object + " from " + h.FromLocation + " to " + h.ToLocation + ". Time: " + mtt(t) + " to " + mtt(t + eta[myMap.get(h.FromLocation)][myMap.get(h.ToLocation)]) + "\n");
            	String tmp2 = "Deliver " + nh.object + " from " + h.FromLocation + " to " + h.ToLocation + ". Time: " + mtt(t) + " to " + mtt(t + eta[myMap.get(h.FromLocation)][myMap.get(h.ToLocation)]) + "\n";
            	returnStr.append(tmp2);
            	returnStr.append("\n");
            	
            	
            	
            }
            System.out.println("Total time needed: " + dist[goal] / 60 + " hours " + dist[goal] % 60 +" minutes.\n");
            String tmp3 = "Total time needed: " + dist[goal] / 60 + " hours " + dist[goal] % 60 +" minutes.\n";
            returnStr.append(tmp3);
            returnStr.append("\n");
            return returnStr.toString();
            
        }
		return "";
    }
    public static int ttm(String str) //time to minute
    {
    	String[] parts = str.split(":");
    	return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
    public static String mtt(int t)
    {
    	if(t% 60 == 0)return String.valueOf(t / 60) + ":00" ;
    	return String.valueOf(t / 60) + ":" + String.valueOf(t % 60);
    }
    
}

class Helper{
	String FromLocation;
	String ToLocation;
	String StartTime; //Time range to leave
	String EndTime; //Time range to leave
	Helper(String FromLocation, String ToLocation, String StartTime, String EndTime)
	{
		this.FromLocation = FromLocation;
		this.ToLocation = ToLocation;
		this.StartTime = StartTime;
		this.EndTime = EndTime;
	}
}

class NeedHelp{
	String object;
	String FromLocation;
	String ToLocation;
	String FromTime;// Package available
	String ToTime;// Package arriving
	NeedHelp(String object,String FromLocation,String ToLocation, String FromTime, String ToTime)
	{
		this.object = object;
		this.FromLocation = FromLocation;
		this.ToLocation = ToLocation;
		this.FromTime = FromTime;
		this.ToTime = ToTime;
	}
	
}