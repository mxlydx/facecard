package cc.gooa.facecard.mapper;

import cc.gooa.facecard.model.CustomerBasicModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerBasicModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerBasicModel record);

    int insertSelective(CustomerBasicModel record);

    CustomerBasicModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerBasicModel record);

    int updateByPrimaryKeyWithBLOBs(CustomerBasicModel record);

    int updateByPrimaryKey(CustomerBasicModel record);

    List<CustomerBasicModel> selectAll(int limit, int offset, int schoolId);

    int selectCountAll();

    List<CustomerBasicModel> selectMan();
}