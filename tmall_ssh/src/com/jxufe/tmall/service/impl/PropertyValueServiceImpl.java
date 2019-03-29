package com.jxufe.tmall.service.impl;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jxufe.tmall.pojo.Product;
import com.jxufe.tmall.pojo.Property;
import com.jxufe.tmall.pojo.PropertyValue;
import com.jxufe.tmall.service.PropertyService;
import com.jxufe.tmall.service.PropertyValueService;
 
@Service(value="propertyValueService")
public class PropertyValueServiceImpl extends BaseServiceImpl implements PropertyValueService {
 
    @Autowired
    PropertyService propertyService;
     
    @Override
    public void init(Product product) {
        List<Property> propertys= propertyService.listByParent(product.getCategory());
        for (Property property: propertys) {
            PropertyValue propertyValue = get(property,product);//通过property和product得到唯一的propertyValue
            if(null==propertyValue){
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                save(propertyValue);
            }
        }
    }
 
    private PropertyValue get(Property property, Product product) {
        List<PropertyValue> result= this.list("property",property, "product",product);
        if(result.isEmpty())
            return null;
        return result.get(0);
    }
     
}