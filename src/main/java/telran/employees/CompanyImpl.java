package telran.employees;

import java.util.*;

public class CompanyImpl implements Company {
    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managersFactor = new TreeMap<>();

    @Override
    public Iterator<Employee> iterator() {
        return new CompanyImplIterator();
    }
    private class CompanyImplIterator implements Iterator<Employee> {

        @Override
        public boolean hasNext() {
            return employees != null;
        }

        @Override
        public Employee next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            
        }
    
        
    }

    @Override
    public void addEmployee(Employee empl) {
        if (employees.containsKey(empl.getId())) {
            throw new IllegalStateException();
        } else {
            employees.put(empl.getId(), empl);
            addToDepartment(empl);
            addToMenegers(empl);
        }
    }

    private void addToMenegers(Employee empl) {
        if (empl instanceof Manager manager) {
            managersFactor.computeIfAbsent(manager.getFactor(), i -> new ArrayList<>()).add(manager);
        }
    }

    private void addToDepartment(Employee empl) {
        String departnent = empl.getDepartment();
        if (departnent != null) {
            employeesDepartment.computeIfAbsent(departnent, i -> new ArrayList<>()).add(empl);
        }
    }

    @Override
    public Employee getEmployee(long id) {
        return employees.get(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        Employee removedEmployee = employees.remove(id);
        if (removedEmployee == null) {
            throw new NoSuchElementException();
        } else {
            removeFromDepartment(removedEmployee);
            removeFromManagers(removedEmployee);
        }
        return removedEmployee;
    }

    private void removeFromManagers(Employee removedEmployee) {
        if (removedEmployee instanceof Manager) {
            Float factor = ((Manager) removedEmployee).getFactor();
            List<Manager> managers = managersFactor.get(factor);
            managers.remove(removedEmployee);
            if (managers.isEmpty()) {
                managersFactor.remove(factor);
            }
        }
    }

    private void removeFromDepartment(Employee removedEmployee) {
        String department = removedEmployee.getDepartment();
        if (department != null) {
            List<Employee> employeesDep = employeesDepartment.get(department);
            employeesDep.remove(removedEmployee);
            if (employeesDep.isEmpty()) {
                employeesDepartment.remove(department);
            }
        }
    }

    @Override
    public int getDepartmentBudget(String department) {
        int budget = 0;
        List<Employee> employees = employeesDepartment.get(department);
        if (employees != null) {
            budget = employees.stream().mapToInt(i -> i.computeSalary()).sum();
        }
        return budget;
    }

    @Override
    public String[] getDepartments() {
        Set<String> departments = employeesDepartment.keySet();
        String[] departmentArray = new String[departments.size()];
        int i = 0;
        for(String department : departments) {
            departmentArray[i] = department;
            i++;
        }
        Arrays.sort(departmentArray);
        return departmentArray;
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
       if(managersFactor.isEmpty()){
        Manager[] res = new Manager[0];
       }
       List<Manager> managersWithFactor = managersFactor.lastEntry().getValue();
        return managersWithFactor.toArray(new Manager[managersWithFactor.size()]);
    }

}