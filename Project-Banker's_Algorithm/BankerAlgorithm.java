import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * Developer:	Giovanni Vecchione
 * 
 * Program #:	4
 * 
 * File Name:	BankerAlgorithm.java
 * 
 * Course:		Operating Systems
 * 
 * Due Date:	5/3/25
 * 
 * Instructor:	Prof. Fred Kumi
 * 
 * Java Version: 8
 * 
 * Description:  Handles bulk of work. Initialization methods,  User Input Loop, Validation, Commands (RQ, RL, OP), and Helper Functions.
 *
 */
public class BankerAlgorithm 
{
	//Init. Variables
	private static final int NUMBER_OF_CUSTOMERS = 5;		  //Set number of Customers per Specs
	private static final int NUMBER_OF_RESOURCES = 4;		  //Set number of resource types per Specs
	private static String fileNameInput = "src/Program4.txt"; //MUST PLACE TXT FILE IN SRC FOLDER
	private Scanner userInputScanner = new Scanner(System.in);//Scanner for User input

	
	//USE ARRAYS AS SPECIFIED IN INSTRUCTIONS
	private int[] available;			//Available amount of each resource
	private int [][] maximum;			//The maximum demand of each customer
	private int[][] allocation;			//The amount currently allocated to each customer
	private int [][] need;				//the remaining need of each customer
	
	//Constructor to Init.
	public BankerAlgorithm()
	{
		//Init. arrays based on constants
		available = new int[NUMBER_OF_RESOURCES];
		maximum = new int [NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
		allocation = new int [NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
		need = new int [NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
		
	}
	
	/*
	 *	Method:			run
	 *
	 *	Description:	Main Flow Control of the program, runs the program
	 *
	 *	Parameters:		N/A
	 *
	 *	Returns:		N/A
	 */
	public void run()
	{		
		//Step 1 - Print Developer info & set scanner
		developerInfo();
		
		//Step 2 - Init. available array to user input.
		initializeAvailable(userInputScanner);
		
		//Step 3 - Init. maximum demand from file
		initializeMaximumAndNeed(fileNameInput);
		
		//Step 4 - User Input - Requests
		userInputLoop(userInputScanner);
		
		//Close Resources
		System.out.println("\nProgram Terminated.\n");
		userInputScanner.close();
		
	}//end of run method
	
	/*
	 *	Method:			userInputLoop
	 *
	 *	Description:	User Input Loop - this is where the user will enter commands to assess
	 *					(RQ) Request Resources - Can be Accepted or Denied (Depends on safety Algorithm)
	 *					(RL) Release Resources
	 *					(OP) Output the values of the different data structs
	 *
	 *	Parameters:		1 - Scanner - UserInput
	 *
	 *	Returns:		N/A
	 */
	public void userInputLoop(Scanner userInput)
	{
		//Set Local Variables
		String command = null;
		int customerNumber = 0;
		int[] dataArray = null;
		String[] parts = null;
		String userInputStr = null;
		
		try{
			//Prompt User
			printInstructions();
			
			userInputStr = userInput.nextLine().trim(); //Read line, remove leading spaces
			
			//CHECK IF INPUT IS EMPTY - REPROMPT if Empty
			if(userInputStr.isEmpty()){
				System.out.println("Expected Exactly " + NUMBER_OF_RESOURCES + " inputs.\n");
				throw new InputMismatchException("Invalid Input size: Please Try Again...\n"); 
			}
			
			//Main Loop
			while(!userInputStr.equalsIgnoreCase("Exit")){
				
				//Split String line
				parts = userInputStr.split(" ");
				
				//Check if input is valid Full command
				//Parse Command
				command = parts[0];
				
				//check for OP command for Output Values
				if(command.equalsIgnoreCase("OP")){
					outputValues();
				}
				//Otherwise continue with process
				else{
					customerNumber = Integer.parseInt(parts[1]);
					dataArray = new int[parts.length - 2];
					
					//Extract values for array - starts at 2 to skip first two parts.
					for (int i = 2; i < parts.length; i++){
						dataArray[i-2] = Integer.parseInt(parts[i]);
					}
					
					//check for other commands
					runCommand(command, customerNumber, dataArray);
				}

				//Reprompt User
				printInstructions();
				userInputStr = userInput.nextLine().trim();
				
				//CHECK IF INPUT IS EMPTY - REPROMPT if Empty
				if(userInputStr.isEmpty()){
					System.out.println("Expected Exactly " + NUMBER_OF_RESOURCES + " inputs.\n");
					throw new InputMismatchException("Invalid Input size: Please Try Again...\n"); 
				}
			}
		}
		catch(NumberFormatException nfe){
			System.out.println("Error in userInputLoop Method: " + nfe.getMessage());
			//REPROMPT
			userInputLoop(userInputScanner);
		}
		catch(InputMismatchException ime){
			System.out.println("Error in userInputLoop Method: " + ime.getMessage());
			//REPROMPT
			userInputLoop(userInputScanner);
		}
		catch(IndexOutOfBoundsException ioobe){
			System.out.println("Error in userInputLoop Method: Index Out of Bounds : " + ioobe.getMessage());
			//REPROMPT
			userInputLoop(userInputScanner);
		}
		
	}//end of inputLoop method
	
	/*
	 *	Method:			printInstructions 
	 *
	 *	Description:	Prints out the Prompt Instructions for User Input
	 *
	 *	Parameters:		N/A
	 *
	 *	Returns:		N/A
	 */
	public void printInstructions()
	{
		System.out.println("\nCommands: \nRQ - Request\nRL - Release\nOP - Output Current Values for Data Structures\n");
		System.out.println("Expected Input Format: Command CustomerNumber Resources1 Resources2 Resources3 Resources4\nJust input 'OP' for output values per specifications.");
		System.out.println("Number of Customers Set for Program: " + NUMBER_OF_CUSTOMERS + "\n");
		System.out.println("Number of Resource Types Set for Program: " + NUMBER_OF_RESOURCES + "\n");
		System.out.println("Input 'Exit' to end program.\n\n");
		System.out.println("Enter Command all together seperated by spaces: ");
	}//end of printInstructions method
	
	/*
	 *	Method:			runCommand 
	 *
	 *	Description:	Selects Command to Run and calls proper methods
	 *
	 *	Parameters:		3 - String, int, int[]
	 *
	 *	Returns:		N/A
	 */
	public void runCommand(String command, int customerNum, int[] resources)
	{
		//Evaluate Commands
		//RQ for Request
		if(command.equalsIgnoreCase("RQ")){
			requestResources(customerNum, resources);
		}
		//RL for Release
		else if(command.equalsIgnoreCase("RL")){
			releaseResources(customerNum, resources);
		}
		else{
			System.out.println("Invalid Command...");
		}
		
	}//end of runCommand Method
	
	/*
	 *	Method:			initializeAvailable 
	 *
	 *	Description:	Sets the number of resources for each resource type, user sets this value.
	 *
	 *	Parameters:		N/A
	 *
	 *	Returns:		N/A
	 */
	public void initializeAvailable(Scanner userInput)
	{
		//Temp Array to assess input
		int tempArray [] = new int [NUMBER_OF_RESOURCES];
		
		String userInputStr = "";						   //String to hold user input from scanner
		
		//ASK FOR IPUT
		System.out.println("Initializing Available Resources...");
		System.out.println("Please enter " + NUMBER_OF_RESOURCES + " non-negative integers separated by spaces on a single line: ");
		
		try{
			//Capture input
			userInputStr = userInput.nextLine().trim();
			
			//CHECK IF INPUT IS EMPTY - REPROMPT if Empty
			if(userInputStr.isEmpty()){
				System.out.println("Expected Exactly " + NUMBER_OF_RESOURCES + " inputs.\n");
				throw new InputMismatchException("Invalid Input size: Please Try Again...\n"); 
			}
			
			//Split String into tokens of resources
			String resources[] = userInputStr.split("\\s+");
			
			//CHECK IF INPUT IS CORRECT SIZE - REPROMPT if NOT right size
			if(resources.length != NUMBER_OF_RESOURCES){
				throw new InputMismatchException("Invalid Input Size: Please Try Again...\n"); 
			}
			//Otherwise continue process
			else{
				
				for (int i = 0; i < NUMBER_OF_RESOURCES; i++){
					//Parse Input as integer into temp array
					tempArray[i] = Integer.parseInt(resources[i]);
					
					//Validate Integers input from user
					if(tempArray[i] > 0){
						available[i] = tempArray[i];
						System.out.println("Resource amount for resource type " + i + ": " + available[i] + "\n");
					}
					//IF NEGATIVE VALUE IS INPUT TRY AGAIN
					else {
						throw new InputMismatchException("Invalid Input: Please Try Again...\n");
					}
				}
			}
			
		}
		catch(NumberFormatException nfe){
			System.err.println("Exception in initializeAvailable method: Invalid Input Format" + nfe.getMessage());
			//REPROMPT
			initializeAvailable(userInputScanner);
		}
		catch(InputMismatchException ime){
			System.err.println("Exception in initializeAvailable method: Input error during available resource initialization" + ime.getMessage());
			//REPROMPT
			initializeAvailable(userInputScanner);
		}
	}//end of initializeAvailable method
	
	
	/*
	 *	Method:			initializeMaximumAndNeed 
	 *
	 *	Description:	Sets the maximum number of requests for each customer, input file sets this value.
	 *					Need is also intialized since need is max - allocation. We just start it at max also.
	 *
	 *	Parameters:		1 - String - FileName
	 *
	 *	Returns:		N/A
	 */
	public void initializeMaximumAndNeed(String fileName)
	{
		File inputFile = new File(fileName); //create file object
		
		//Use try-with-resources for automatic closing of the Scanner
		try (Scanner fileScanner = new Scanner(inputFile)){
			System.out.println("Reading Max demand from file: " + fileName + "...");
			
			//loop through expected customers (rows)
			for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++){
				//CHECK if there is a next line available for the current customer
				if(!fileScanner.hasNextLine()){
					throw new InputMismatchException("No next line for current customer " + i + "...");
				}
				
				//Get each line
				String line = fileScanner.nextLine().trim(); //Read line, remove leading spaces
				
				//Check for empty lines
				if(line.isEmpty()){
					throw new InputMismatchException("Empty input for current customer " + i + "..."); 
				}
				
				//Split the line into resource values
				String[] resources = line.split("\\s+");
				
				//Check if number of values on the line correct
				if(resources.length != NUMBER_OF_RESOURCES){
					throw new InputMismatchException("Incorrect input size for current customer " + i + "..."); 
				}
				
				//Parse each token on the line
				for (int j = 0; j < NUMBER_OF_RESOURCES; j++){
					int maxDemand = Integer.parseInt(resources[j]); //Parse String token to int
					
					//validate - Max Demand cannot be negative
					if(maxDemand < 0){
						throw new InputMismatchException("Negative value found for current customer " + i + "..."); 
					}
					
					//Assign Valid value to max and need arrays.
					this.maximum[i][j] = maxDemand;
					this.need[i][j] = maxDemand; //Initialize the need which is maximum - allocation
				}
			}
			
			System.out.println("File Valid: Maximum Set...\n");
		}
		catch(InputMismatchException ime){
			System.err.println("Exception Occured in initializeMaximum Method: " + ime.getMessage());
		}
		catch(NumberFormatException nfe){
			System.err.println("Exception Occured in initializeMaximum Method: " + nfe.getMessage());
		}
		catch(FileNotFoundException fnfe){
			System.err.println("Exception Occured in initializeMaximum Method: " + fnfe.getMessage());
		}
		
	}//end of initializeMaximum Method
	
	
	
	/*
	 *	Method:			requestResources (RQ)
	 *
	 *	Description:	method to request resources
	 *
	 *	Parameters:		2 - Int, Int - customerNum, request
	 *
	 *	Returns:		1 - boolean - T (Successful) / F (Unsuccessful)
	 */
	public boolean requestResources(int customerNum, int[] resources)
	{
		boolean validRequestSafety = false;	   //Checks banker's algorithm
		boolean validRequestResources = false; //checks for request against resources available and need.
		int customerNumRQ = customerNum;
		int [] customerRequestRQ = resources;
		boolean validRequest = false; 		   //Check overall request
		
		//Save Current State
		int [] originalAvailable = available.clone();
		int[] originalAllocation = allocation[customerNumRQ].clone();
		int[] originalNeed = need[customerNumRQ].clone();
		
		//validate available resources and need
		validRequestResources = validateNeedAndAvailable(customerNumRQ, customerRequestRQ);
		//Validate Safety based on banker's algorithm
		validRequestSafety = validateRequestSafety(customerNumRQ, customerRequestRQ);
		
		//If either validate check is false consider it as UNSAFE
		if (validRequestSafety == false || validRequestResources == false){
			System.out.println("Safety Check Result: UNSAFE\n");
			System.out.println("Request DENIED...\n");
		}
		//Otherwise good to go
		else{
			System.out.println("Safety Check Result: SAFE\n");
			System.out.println("Request Accepted...\n");			
			validRequest = true;
			
			//Set Allocation
			System.out.println("Applying allocation...\n");
			for(int j = 0; j < NUMBER_OF_RESOURCES; j++){
				available[j] -= customerRequestRQ[j];
				allocation[customerNumRQ][j] += customerRequestRQ[j];
				need[customerNumRQ][j] -= customerRequestRQ[j];
			}
			
			System.out.println("Arrays Updated...\n");
		}
		
		return validRequest; //TO-DO CHANGE THIS
		
	}//end of requestResources method
	
	/*
	 *	Method:			validateNeedAndAvailable
	 *
	 *	Description:	Checks to ensure that need and available won't go negative
	 *
	 *	Parameters:		2 - int , int[][]
	 *
	 *	Returns:		1 - boolean - t/f
	 */
	public boolean validateNeedAndAvailable(int customerNum, int[] request) 
	{
		boolean validRequest = true;
		
		for (int j = 0; j < NUMBER_OF_RESOURCES; j++){
			//check for invalid negative requests first
			//check available
			if(request[j] > available[j]){
				System.err.println("Request will cause a negative value for available.\n");
				validRequest = false;
			}
			//check need
			else if(request[j] > need[customerNum][j]){
				System.err.println("Request will cause a negative value for need.\n");
				validRequest = false;
			}
			
		}
		
		
		return validRequest;
	}//end of validateNeedAndAvailable method

	/*
	 *	Method:			releaseResources (RL)
	 *
	 *	Description:	method to release resources
	 *
	 *	Parameters:		2 - Int, Int - customerNum, release
	 *
	 *	Returns:		N/A
	 */
	public void releaseResources(int customerNum, int[] resources)
	{
		boolean validRelease = false;
		int customerNumRL = customerNum;
		int [] customerReleaseRL = resources;
		
		System.out.println("Validating Release...\n");
		validRelease = validateReleaseSafety(customerNumRL, customerReleaseRL);
		
		if (validRelease == false){
			System.out.println("Valid Release: INVALID\n");
		}
		else{
			System.out.println("Valid Release: VALID\n");
			System.out.println("Updating Arrays...\n");
			
			//Perform Updates
			for(int i = 0; i < NUMBER_OF_RESOURCES; i++){
				//add resources back to available pool
				available[i] += customerReleaseRL[i];
				
				//Subtract resources from customer allocation
				allocation[customerNumRL][i] -= customerReleaseRL[i];
				
				//add resources back to customer's need
				need[customerNumRL][i] += customerReleaseRL[i];
			}
			
			System.out.println("Arrays Updated Successfully...\n");

		}
		
	}//end of releaseResources method
	
	/*
	 *	Method:			releaseResources (RL)
	 *
	 *	Description:	method to release resources
	 *
	 *	Parameters:		2 - Int, Int - customerNum, release
	 *
	 *	Returns:		N/A
	 */
	public boolean validateReleaseSafety(int customerNumRL, int [] customerReleaseRL)
	{
		boolean validRelease = false;
		
		try{
			for(int i = 0; i < NUMBER_OF_RESOURCES; i++){
				//Check if customer release amount is negative or is greater than the allocation amount already provided.
				if(customerReleaseRL[i] <= allocation[customerNumRL][i] && customerReleaseRL[i] > 0){
					validRelease = true;
				}
			}
		}
		catch(IndexOutOfBoundsException ioobe){
			System.err.println("Error in validateReleaseSafety: Index Issue" + ioobe.getMessage());
		}	
		
		return validRelease;	
		
	}//End of validateReleaseSafety method
	
	
	/*
	 *	Method:			outputValues (OP)
	 *
	 *	Description:	method to output the valuesj of the different data structures being used.
	 *
	 *	Parameters:		2 - Int, Int - customerNum, release
	 *
	 *	Returns:		N/A
	 */
	public void outputValues()
	{
		//PRINT DATA STRUCTURES
		System.out.print("Outputting Data Structure Values...\n"); //Print element and a tab
		
		System.out.print("Outputting Maximum Values: "); //Print element and a tab
		print2DArray(this.maximum);
		
		System.out.print("\nOutputting Need Values: "); //Print element and a tab
		print2DArray(this.need);
		
		System.out.print("\nOutputting Allocation Values: "); //Print element and a tab
		print2DArray(this.allocation);
		
		System.out.print("\nOutputting Available Values:\t"); //Print element and a tab
		printArray(this.available);
	}//end of outputValues method
	
	/*
	 *	Method:			validateRequestSafety
	 *
	 *	Description:	Validates if user request is safe (Implements Banker's algorithm)
	 *
	 *	Parameters:		1 - int Array[]
	 *
	 *	Returns:		1 - Boolean
	 */
	
	//ISSUES WITH THIS - ALGORITHM ISNT WORKING
	public boolean validateRequestSafety(int customerNumRQ, int array[]){
		// Step 1: Initialize Work and Finish
	    int[] work = available.clone();
	    boolean[] finish = new boolean[NUMBER_OF_CUSTOMERS]; // Defaults to false
	    int finishedCount = 0;

	    // Use a 'for' loop for passes. In the worst case for a safe state,
	    // we might need up to NUMBER_OF_CUSTOMERS passes (each finishing one process).
	    // We also need a flag to stop early if no progress is made.
	    boolean progressInPreviousPass = true; // Start true to allow the first pass

	    // Loop a maximum of N times, but also stop if no progress was made
	    for (int pass = 0; pass < NUMBER_OF_CUSTOMERS && progressInPreviousPass; pass++) {

	        boolean progressThisPass = false; // Reset progress tracking for the current pass
	        // Inner loop: Iterate through each customer (process)
	        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
	            // Consider only processes not yet finished
	            if (!finish[i]) {
	                // Check if this process's needs can be met by 'work'
	                boolean needMet = true; // Assume need is met
	                
	                for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
	                    if (need[i][j] > work[j]) {
	                        needMet = false;
	                        // No break; loop must finish checking resources
	                    }
	                } // End resource check loop (j)

	                // If all needs were met
	                if (needMet) {
	                    // --- Step 3: Simulate finishing process i ---
	                    for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
	                        work[j] += allocation[i][j];
	                    }
	                    finish[i] = true;
	                    finishedCount++;
	                    progressThisPass = true; // Progress was made in this pass
	                    // Continue checking other customers in this pass
	                } // End if(needMet)
	            } // End if(!finish[i])
	        } // End inner for loop (checking customers i)

	        // Update the flag for the next iteration's check
	        progressInPreviousPass = progressThisPass;

	        // If progressInPreviousPass is now false, the outer loop's
	        // condition (&& progressInPreviousPass) will fail on the next check,
	        // terminating the loop gracefully.

	    } // End outer for loop (passes)

	    // Step 4: Final determination after the loop finishes
	    // If the count of finished processes equals the total number, the state is safe.
	    return (finishedCount == NUMBER_OF_CUSTOMERS);
	    
	}//end of validateRequestSafety method
	
	
	/*
	 *	Method:			print2DArray
	 *
	 *	Description:	method to print an int 2D array
	 *
	 *	Parameters:		1 - int array[][]
	 *
	 *	Returns:		N/A
	 */
	public void print2DArray(int array2D[][])
	{
		//Iterate through each row of the matrix
		for (int[] row : array2D){
			
			System.out.print("\n"); //Divide each row.
			
			//iterate through each element
			for(int element : row) {
				
				System.out.print(element + "\t"); //Print element and a tab
			}
		}
		
		System.out.print("\n"); //SPACE
		
	}//end of print2DArray method
	
	/*
	 *	Method:			printArray
	 *
	 *	Description:	method to print an int array
	 *
	 *	Parameters:		1 - int array[]
	 *
	 *	Returns:		N/A
	 */
	public void printArray(int array[])
	{
		//Iterate through elements
		for(int element : array){
			System.out.print(element + "\t"); //Print element and a tab
		}
		
		System.out.print("\n"); //SPACE

	}//end of printArray Method
	
	
	
	
	
	//***************************************************************
    //
    //  Method:       developerInfo
    // 
    //  Description:  The developer information method of the program
	//                This method and comments must be included in all
	//                programming assignments.
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public void developerInfo()
    {
       System.out.println("Name:     Giovanni Vecchione");
       System.out.println("Course:   COSC 4302 - Operating Systems");
       System.out.println("Program:  4");
	   System.out.println("Due Date: 5/3/25\n");	   
    } // End of the developerInfo method
}//end of BankerAlgorithm Class
