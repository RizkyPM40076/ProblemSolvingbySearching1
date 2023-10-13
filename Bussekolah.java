import java.util.*;

class Node {
    int id;
    int demand;

    public Node(int id, int demand) {
        this.id = id;
        this.demand = demand;
    }
}

public class SchoolBusRoutingProblem {
    private static List<Node> students;
    private static int busCapacity;

    public SchoolBusRoutingProblem(int busCapacity, List<Node> students) {
        this.busCapacity = busCapacity;
        this.students = students;
    }

    public static void main(String[] args) {
        List<Node> students = new ArrayList<>();
        students.add(new Node(0, 0)); // Depot
        students.add(new Node(1, 7));
        students.add(new Node(2, 5));
        students.add(new Node(3, 8));
        students.add(new Node(4, 6));
        students.add(new Node(5, 3));
        students.add(new Node(6, 2));
        students.add(new Node(7, 1));
        students.add(new Node(8, 9));
        students.add(new Node(9, 4));

        int busCapacity = 15;
        int populationSize = 50;
        int generations = 100;
        double mutationRate = 0.1;

        SchoolBusRoutingProblem problem = new SchoolBusRoutingProblem(busCapacity, students);

        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(problem.createIndividual());
        }

        for (int generation = 0; generation < generations; generation++) {
            List<Double> fitnessScores = new ArrayList<>();
            for (List<Integer> individual : population) {
                fitnessScores.add(problem.calculateFitness(individual));
            }

            List<List<Integer> selectedPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                if (Math.random() < fitnessScores.get(i)) {
                    selectedPopulation.add(population.get(i));
                }
            }

            List<List<Integer> offspring = new ArrayList<>();
            while (offspring.size() < populationSize) {
                List<Integer> parent1 = selectedPopulation.get(new Random().nextInt(selectedPopulation.size()));
                List<Integer> parent2 = selectedPopulation.get(new Random().nextInt(selectedPopulation.size()));
                int crossoverPoint = new Random().nextInt(students.size() - 1) + 1;
                List<Integer> child = new ArrayList<>();
                child.addAll(parent1.subList(0, crossoverPoint));
                child.addAll(parent2.subList(crossoverPoint, students.size()));
                offspring.add(child);
            }

            for (int i = 0; i < populationSize; i++) {
                if (Math.random() < mutationRate) {
                    int index1 = new Random().nextInt(students.size());
                    int index2 = new Random().nextInt(students.size());
                    Collections.swap(population.get(i), index1, index2);
                }
            }

            population = offspring;
        }

        List<Integer> bestIndividual = null;
        double bestFitness = -1;
        for (List<Integer> individual : population) {
            double fitness = problem.calculateFitness(individual);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestIndividual = individual;
            }
        }

        System.out.println("Rute Terbaik: " + bestIndividual);
        System.out.println("Fitness Terbaik: " + bestFitness);
    }

    public List<Integer> createIndividual() {
        List<Integer> individual = new ArrayList<>();
        for (int i = 1; i < students.size(); i++) {
            individual.add(i);
        }
        Collections.shuffle(individual);
        individual.add(0, 0); // Depot sebagai titik awal dan akhir
        return individual;
    }

    public double calculateFitness(List<Integer> individual) {
        double totalDistance = 0;
        double currentCapacity = 0;

        for (int i = 1; i < students.size(); i++) {
            int studentId = individual.get(i);
            int demand = students.get(studentId).demand;

            if (currentCapacity + demand > busCapacity) {
                totalDistance += distanceBetween(students.get(individual.get(i - 1)), students.get(studentId));
                currentCapacity = demand;
            } else {
                currentCapacity += demand;
            }
        }

        return 1 / (totalDistance + 1);
    }

    public double distanceBetween(Node node1, Node node2) {
        // Fungsi untuk menghitung jarak antara dua node (misalnya, dengan koordinat GPS)
        return Math.abs(node1.id - node2.id);
    }
}
