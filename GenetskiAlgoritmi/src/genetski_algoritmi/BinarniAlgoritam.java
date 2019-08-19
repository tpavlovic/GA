package genetski_algoritmi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.*;
import java.util.Arrays;


public class BinarniAlgoritam {
	
	public static int[] chromosomeNumbers = {20, 100, 150};
	private int numberOfGeneration = 500;
	private int numberOfBits = 10;
	private double intervalLeftEnd = -4.5;
	private double intervalRightEnd = 4.5;
	private int mutationRate = 3; 
	private int[][] population;
	private int[][] newPopulation;
	private int[][] newTwo = new int[2][numberOfBits];
	private int[] arrayAfterMutation = new int[numberOfBits];
	private int[] first = new int[10];
	private int[] second = new int[10];
	private ArrayList<Double> bestChromosomes;
	int[] numberForCalculate = new int[numberOfBits];
	double currentSum, sum = 0.0, best;
	
	
	public BinarniAlgoritam() {
	}
	
	
	public double billFunction(double x, double y) {
		return Math.pow((1.5 - x + x*y), 2) + Math.pow((2.25 - x + x*Math.pow(y, 2)), 2) + 
			   Math.pow((2.625 - x + x*Math.pow(y, 3)),2);
	}
	
	
	public void mainFunction() {
		System.out.println("Number Of generation: " + numberOfGeneration);
		System.out.println("Number of bits: " + numberOfBits);
		System.out.println("Mutation rate: " + mutationRate + "\n");
		
		bestChromosomes = new ArrayList<Double>();
		
		for (int numberOfChromosome : chromosomeNumbers) { //20, 100, 150
			bestChromosomes = new ArrayList<>();
			population = new int[numberOfChromosome][numberOfBits];
			newPopulation = new int[population.length][numberOfBits];
			System.out.println("------------------------------------");
			System.out.println("Chromosome number: " + numberOfChromosome + "\n");
			
			for(int j = 1; j <= 5; j++) { //run five times
				//create population
				for(int l = 0; l < numberOfChromosome; l++) { 
					for(int k=0; k<numberOfBits; k++) { 
						population[l][k] = ((int) (Math.random() * 2));
					}
				}
				
			
			//run genetic algorithm
			for (int i = 0; i < numberOfGeneration; i++) { //number of genetarions
					
			Arrays.sort(population, new Comparator<int[]>() {
				@Override
				public int compare(int[] o1, int[] o2) {
							
					String str1 = "", str2 = "";
					int num1=0, num2=0;
							
					for(int i=0; i<numberOfBits; i++) {
						str1 += o1[i];
						str2 += o2[i];
					}
							
					num1 = (int)Integer.parseInt(str1);
					num2 =(int) Integer.parseInt(str2);
							
					if(num1 < num2) {
						return 1;
					} else if(num1 > num2) {
						return -1;
					} else {
						return 0;
					}
				}
			});
					
			
			double sum = 0.0;
			for (int k = 0; k < population.length; k++) {
				sum += calculateFunction(population[k]);
			}
					
					
			//create new hromozomes
			for(int l = 0; l < population.length/2; l++) {  //20, 100, 150 hromozoma po iteracijama
				for(int k=0; k < numberOfBits; k++) { //40 bitova svaki hromozom
					newPopulation[l][k] = population[l][k];
				}
			}

			int brojac = 0;
			for (int k = 0; k < (newPopulation.length/2)/2; k++) {
				for (int m = 0; m < numberOfBits; m++) {
					first[m] = newPopulation[2*k][m];
					second[m] = newPopulation[2*k+1][m];
					if(m == 9) {
						reproduce(first, second, k);
						for (int l = 0; l < 1; l++) {
							for (int l2 = 0; l2 < numberOfBits; l2++) {
								newPopulation[(newPopulation.length/2) + 2*k +0][l2] = newTwo[0][l2];
								newPopulation[(newPopulation.length/2) + 2*k +1][l2] = newTwo[1][l2];
							}	
						}
					}
				}
			}

					
			//mutate new hromozomes
			for (int l = newPopulation.length/2; l < newPopulation.length; l++) {
				int[] array = new int[numberOfBits];
				for (int l2 = 0; l2 < numberOfBits; l2++) {
					array[l2] = newPopulation[l][l2];
				}
				
				arrayAfterMutation = mutation(array);
					
				for (int k = 0; k < numberOfBits; k++) {
					newPopulation[l][k] = arrayAfterMutation[k];
				}
			}
					
			best = Double.POSITIVE_INFINITY;
			for (int ik = 0; ik < newPopulation.length; ik++) {
				for (int k = 0; k < numberOfBits; k++) {
					numberForCalculate[k] = newPopulation[ik][k];
				}
				currentSum = calculateFunction(numberForCalculate);
				if(best > currentSum) {
					best = currentSum;
				}
			}
								
			bestChromosomes.add(best);
		}
				
				
			for (int ik = 0; ik < newPopulation.length; ik++) {
				for (int k = 0; k < numberOfBits; k++) {
					numberForCalculate[k] = newPopulation[ik][k];
				}
				currentSum = calculateFunction(numberForCalculate);
				sum += currentSum;
			}
			
			//sum = average hromozome
			sum /= newPopulation.length;
				
				
			double theBestInArray = Double.POSITIVE_INFINITY;
			for (double b : bestChromosomes) {
				if(theBestInArray > b) {
					theBestInArray = b;
				}
			}	
					
			System.out.println("ITERATION: " + j);
			System.out.println("BEST: " + best);
			System.out.println("AVERAGE	: " + sum + "\n");

		}
			
			
			double bestChromosome = 0;
			for (double array : bestChromosomes) {
				if(bestChromosome < array) {
					bestChromosome = array;
				}
			}
			
			System.out.println("The best: " + bestChromosome + "\n");
			
		}
		
	}
	
	public int funckijaTroska(int chromosome[]) {
		int count = 0;
		
		for (int i = 0; i < chromosome.length; i++) {
			if(chromosome[i] == 1) {
				count++;
			}
		}
		
		return count;
	}
	
	//Converts binary hromozome into real number
	public double[] chromosomeToNumber(int[] chromosome) {
		int counter = 0;
		double number1, number2, number3;
		double x, y;
		double sum1 = 0.0, sum2 = 0.0;
		double[] res = new double[2];
		
		for (int i = chromosome.length-1; i >= chromosome.length/2; i--) {
			number1 = chromosome[i];
			number2 = Math.pow(2, counter);
			number3 = number1 * number2;
			sum1 += number3;
			counter++;
		}

		x = intervalLeftEnd + sum1 * ((intervalRightEnd - intervalLeftEnd)/(Math.pow(2, counter)-1));
		counter = 0;
		
		for (int j = chromosome.length/2-1; j >= 0; j--) {
			number1 = chromosome[j];
			number2 = Math.pow(2, counter);
			number3 = number1 * number2;
			
			sum2 += number3;
			counter++;
		}
		
		y = intervalLeftEnd + sum2 * ((intervalRightEnd - intervalLeftEnd)/(Math.pow(2, counter)-1));
		
		res[0] = x;
		res[1] = y;
		
		return res;
	}
	
	
	//Creates offspring from provided chromozomes
	public int[][] reproduce(int[] first, int[] second, int k) {		
		int[][] res = new int[2][8];
		int bitInArray1, bitInArray2, newValue;
		
		//change value in first array
		bitInArray1 = first[first.length/2 - 1];
		bitInArray2 = first[first.length/2];
		
		newValue = inverseFunction(bitInArray1);
		first[first.length/2-1] = newValue;
		
		newValue = inverseFunction(bitInArray2);
		first[first.length/2] = newValue;
		
		//change value in second array
		bitInArray1 = second[second.length/2 - 1];
		bitInArray2 = second[second.length/2];
		
		newValue = inverseFunction(bitInArray1);
		second[second.length/2-1] = newValue;
		
		newValue = inverseFunction(bitInArray2);
		second[second.length/2] = newValue;
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < numberOfBits; j++) {
				newTwo[i][j] = first[j];
				newTwo[i+1][j] = second[j];
			}
		}
		
		res[0] = first;
		res[1] = second;
		
		return res;
		
	}
	
	
	//changing 0 into 1 and reversed
	private int inverseFunction(int value) {
		int res;
		
		if(value == 1) {
			res = 0;
		} else {
			res = 1;
		}
		
		return res;
	}
	
	
	//Mutates a chromozome
	public int[] mutation(int[] a) {
		int randomNumber;
		
		for (int i = 0; i < a.length; i++) {
			randomNumber = (int) (Math.random()*100) + 1;
			if(randomNumber <= mutationRate) {	
				a[i] = inverseFunction(a[i]);
			}
		}
		
		return a;
	}
	
	
	//Function to minimize
	public double calculateFunction(int[] a) {
		double[] result = new double[2];
		result = chromosomeToNumber(a);
		
		return billFunction(result[0], result[1]);
	}

}
