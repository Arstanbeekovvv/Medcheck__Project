package Service;

import DAO.Impl.DoctorDAOImpl;
import DAO.Impl.HospitalDAOImpl;
import Models.Department;
import Models.Doctor;
import Models.Gnrted;
import Models.Hospital;

import java.util.ArrayList;
import java.util.List;

public class DoctorServiceImpl implements ZDoctorService {
    DoctorDAOImpl doctorDAO = new DoctorDAOImpl();
//**********************************************************************************************************************
    public DoctorServiceImpl(DoctorDAOImpl doctorDAO) {
        this.doctorDAO = doctorDAO;
    }
    public DoctorServiceImpl() {
    }
//**********************************************************************************************************************
    @Override
    public Doctor findDoctorById(Long id) {
        List<Hospital> all = doctorDAO.getAll();
        for (Hospital hospital : all) {
            for (Doctor doctor : hospital.getDoctors()) {
                if(doctor.getId().equals(id)){
                    System.out.println("Successful find doctor 'DOCTOR' list by id 🙋🏻‍♂️");
                    return doctor;
                }
            }
            for (Department department : hospital.getDepartments()) {
                for (Doctor doctor : department.getDoctors()) {
                    if(doctor.getId().equals(id)){
                        System.out.println("Successful find doctor from 'DEPARTMENT' list by id 🙋🏻‍♂️");
                        return doctor;
                    }
                }
            }
        }
        System.out.println("NOT successful find doctor 'DOCTOR' list by id  🙅🏻‍♂️️");
        return null;
    }

    @Override
    public List<Doctor> getAllDoctorsByHospitalId(Long id) {
        List<Hospital> all = doctorDAO.getAll();
        for (int i = 0; i < all.size(); i++) {
            if(all.get(i).getId().equals(id)){
                System.out.println("Successful get all doctors by hospital id ✅");
                return doctorDAO.get(i).getDoctors();
            }
        }
        System.out.println("Not find hospital by id!");
        return null;
    }

    @Override
    public List<Doctor> getAllDoctorsByDepartmentId(Long id) {
        List<Hospital> all = doctorDAO.getAll();
        for (int i = 0; i < all.size(); i++) {
            for (Department department : all.get(i).getDepartments()) {
                if (department.getId().equals(id)) {
                    System.out.println("Successful get all doctors by department id✅");
                    return doctorDAO.get(i).getDoctors();
                }
            }
        }
        System.out.println("Not find department by id !!!");
        return null;
    }

    @Override
    public String add(Long hospitalId, Doctor o) {
        try {
            List<Hospital> all = doctorDAO.getAll();
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId().equals(hospitalId)) {
                    doctorDAO.add(i, o);
                    return "Successful add doctor to hospital ✅";
                }
            }
        }catch (NullPointerException npe){
            System.out.println("Null point exception !");
        }
        return "Not successful add doctor !";
    }

    @Override
    public void removeById(Long id) {
        for (int i = 0; i < doctorDAO.getAll().size(); i++) {
            for (int i1 = 0; i1 < doctorDAO.get(i).getDoctors().size(); i1++) {
                if(doctorDAO.get(i).getDoctors().get(i1).getId().equals(id)){
                    doctorDAO.delete(i, i1);
                    System.out.println("Successful remove by id ✅");
                    return;
                }
            }
        }
        System.out.println("Not successful remove by id !");
    }

    @Override
    public String updateById(Long id, Doctor o) {
        for (int i = 0; i < doctorDAO.getAll().size(); i++) {
            for (int i1 = 0; i1 < doctorDAO.get(i).getDoctors().size(); i1++) {
                if(doctorDAO.get(i).getDoctors().get(i1).getId().equals(id)){
                    doctorDAO.update(i, i1, o);
                    System.out.println("Successful update by id ✅");
                }
            }
        }
        return "Not successful update by id !";
    }

    @Override
    public String assignDoctorToDepartment(Long departmentId, List<Long> doctorsId) {
        boolean isTrue = true;
        int count = 0;
        int currentDoctor = 0;
        List<Long> id = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        List<Doctor> doctors = new ArrayList<>();
        for (Long l : doctorsId) {
            for (int i = 0; i < doctorDAO.getAll().size(); i++) {
                for (int i1 = 0; i1 < doctorDAO.get(i).getDepartments().size(); i1++) {
                    if(doctorDAO.get(i).getDepartments().get(i1).getId().equals(departmentId)){
                        for (int i2 = 0; i2 < doctorDAO.get(i).getDoctors().size(); i2++) {
                            if(doctorDAO.get(i).getDoctors().get(i2).getId().equals(l)){
                                doctorDAO.get(i).getDepartments().get(i1).getDoctors().add(doctorDAO.get(i).getDoctors().get(i2));
                                doctors.add(doctorDAO.get(i).getDoctors().get(i2));
                                id.add(l);
                                index.add(i2);
                                isTrue = false;
                                currentDoctor = i;
                            }
                            if(!isTrue) {
                                count++;
                                System.out.println("Successful find doctor by this id -> " + l + "♻️");
                            }
                            isTrue = true;
                        }
                    }

                }
            }
        }
        if(count == doctorsId.size()){
            doctorDAO.get(currentDoctor).getDoctors().removeAll(doctors);
            return "Successful assign all doctors ✅🙋🏻‍♂️";
        }else doctorDAO.get(currentDoctor).getDepartments().forEach(department -> department.getDoctors().removeAll(doctors));
        for (Long l : doctorsId) {
            if(!id.contains(l)){
                System.out.println("Not successful find doctor by this id -> " + l + "🙅🏻‍♂️");
            }
        }
        return "NOT successful find doctor 'DOCTOR' list by id  🙅🏻‍♂️️";
    }
}
