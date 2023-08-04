package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.BrandService;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.increff.pos.util.ConversionUtil.convert;
import static com.increff.pos.util.NormaliseUtil.normalize;
import static com.increff.pos.util.ValidateUtil.validate;

@Component
public class brandDto {
    @Autowired
    private BrandService brandService;

//    @Value("${outputErrorFileDirectory}")
//    private String outputErrorFileDirectory;

    public void add(BrandForm form) throws ApiException {
        normalize(form);
        validate(form);
        BrandPojo p = convert(form);// todo: to use copy bean and create a generic function
        brandService.add(p);
    }
    public BrandData getBrand(Integer id) throws ApiException {
        BrandPojo brandPojo = brandService.getCheck(id);
        return convert(brandPojo);
    }

    public List<BrandData> getAll() { //todo: change names of list
        List<BrandPojo> list = brandService.getAll();
        List<BrandData> list2 = new ArrayList<>();
        for (BrandPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(Integer id, BrandForm f) throws ApiException {
        normalize(f);
        validate(f);
        BrandPojo p = convert(f);
        brandService.update(id, p.getBrand(),p.getCategory());
    }

//    public void addBrandList(List<BrandForm> brandForms) throws ApiException, JsonProcessingException {
//        listEmptyCheck(brandForms);
//        List<BrandErrorData> errorData = new ArrayList<>();
//        Integer errorSize = 0;
//        for (BrandForm brandForm : brandForms) {
//            BrandErrorData brandErrorData = ConvertorUtil.convertToErrorData(brandForm);
//            try {
//                ValidateUtil.validateForms(brandForm);
//                NormaliseUtil.normalise(brandForm);
//                brandService.checkAlreadyExist(brandForm.getBrand(), brandForm.getCategory());
//            } catch (Exception e) {
//                errorSize++;
//                brandErrorData.setMessage(e.getMessage());
//            }
//            errorData.add(brandErrorData);
//        }
//        if (errorSize > 0) {
//            ErrorUtil.throwErrors(errorData);
//        }
//        bulkAdd(brandForms);
//    }

   // @Transactional(rollbackOn = ApiException.class)
    public void addList(List<BrandForm> formList) throws ApiException {
        List<BrandPojo> brandPojoList = new ArrayList<>();
        if (formList.size() == 0) {
            throw new ApiException("List size cannot be zero");
        }
        List<Pair<Integer, String>> errorList = new ArrayList<>();
        Set<String> checkDuplicate = new HashSet<>();
        for (int i = 0; i < formList.size(); i++) {
            BrandForm brandForm = formList.get(i);
            Pair<Integer, String> errorPair;
            try {
                normalize(brandForm);
                validate(brandForm);
                brandService.checkAll(brandForm.getBrand(),brandForm.getCategory());
                BrandPojo brandPojo= convert(brandForm);
                String brandCategoryKey = brandPojo.getBrand() + "#" + brandPojo.getCategory();
                if (!checkDuplicate.add(brandCategoryKey)) {
                    throw new ApiException("Duplicate entry for Brand: " + brandPojo.getBrand() + " Category: " + brandPojo.getCategory());
                }
                brandPojoList.add(brandPojo);
            } catch (ApiException e) {
                errorPair = new Pair<>(i , e.getMessage());
                errorList.add(errorPair);
            }
        }
        System.out.println(checkDuplicate.size());
        if(!errorList.isEmpty()){
            throw new ApiException(errorList.toString());
        }else{
            brandService.bulkAdd(brandPojoList);
        }
    }

//    @Transactional(rollbackOn = ApiException.class)
//    void bulkAdd(List<BrandPojo> list) throws ApiException {
//        for (BrandPojo p:list) {
//            brandService.add(p);
//        }
//    }

//    private void convertFormToErrorFileTsv(List<BrandForm> brandFormList,List<Pair<Integer, String>> errorList){
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputErrorFileDirectory+"/brand-upload-error.tsv",false))) {
//            writer.write("Brand\tCategory\tError\n");
//            for(int i=0;i<brandFormList.size(); i++){
//                String errorMessage = errorList.get(i).getValue();
//                writer.write(brandFormList.get(i).getBrand() + "\t" + brandFormList.get(i).getCategory() + "\t"+ errorMessage+"\n");
//            }
//        } catch (IOException e) {
//            System.err.println("Error writing TSV file: " + e.getMessage());
//        }
//    }
}
// todo: to use copy bean and create a generic function
