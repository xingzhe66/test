package yunyun.mapper;

import yunyun.entity.CifClientPo;

public interface CifClientPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cif_client
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long clientKey);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cif_client
     *
     * @mbggenerated
     */
    int insert(CifClientPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cif_client
     *
     * @mbggenerated
     */
    int insertSelective(CifClientPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cif_client
     *
     * @mbggenerated
     */
    CifClientPo selectByPrimaryKey(Long clientKey);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cif_client
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CifClientPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cif_client
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CifClientPo record);
}