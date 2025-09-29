/**
 * 
 * Developer:	Giovanni Vecchione
 * 
 * Program #:	4
 * 
 * File Name:	Program4.java
 * 
 * Course:		Operating Systems
 * 
 * Due Date:	5/3/25
 * 
 * Instructor:	Prof. Fred Kumi
 * 
 * Java Version: 8
 * 
 * Description:  Main Class*
 * 				 Core Idea is to use the Banker's algorithm as resource allocation management in an operating system.
 * 				 The goal is to ensure the system remains in a safe state, meaning there's a sequence in which all processes
 * 				 can complete their work without causing a deadlock.
 * 				 
 * 				 GOAL: Tha "banker" only grants a resource request if doing so leaves the system in a safe state. Requests leading
 * 				 to an unsafe state is denied.
 *
 */
public class Program4 
{
    
	/*
	 *	Method:			Main
	 *
	 *	Description:	The main method of the program / Starts up Program
	 *
	 *	Parameters:		String Array
	 *
	 *	Returns:		N/A
	 */
	
	public static void main(String[] args)
	{
		
		BankerAlgorithm Banker = new BankerAlgorithm();
    	Banker.run(); 						// Delegate all work to a non-static method
    	
	}//End of Main Method
	
}
