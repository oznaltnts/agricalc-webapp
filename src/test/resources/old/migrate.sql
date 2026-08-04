insert into `crops` (`status`, `group_name`, `name`)
select 1, tcg.name, tc.name
from tvys.crop tc
         join tvys.crop_group tcg on tc.crop_group_id = tcg.id
where tc.name != 'Test'
  and tcg.name != 'Test'
group by tcg.name, tc.name
;

insert into tektarim.questions (`status`, `type`, `value`)
select 1, question_type, question
from tvys.income_expense_question
order by id
;

insert into tektarim.city_crop_question_values (`idate`, `city_crop_question_id`, `value`)
select t1.udate, t11.id, t1.answer_new
from tvys.income_expense_answer_activity t1
         inner join tvys.income_expense_answer t2 on t1.income_expense_answer_id = t2.id
         inner join tvys.income_expense_question t3 on t2.question_id = t3.id
         inner join tektarim.questions t4 on t3.question = t4.value
         inner join tvys.city_crop t5 on t5.id = t2.city_crop_id
         inner join tvys.city t6 on t6.id = t5.city_id
         inner join tektarim.cities t7 on t7.name = t6.name
         inner join tvys.crop t8 on t8.id = t5.crop_id
         inner join tektarim.crops t9 on t9.name = t8.name
         inner join tektarim.city_crops t10 on t10.city_id = t7.id and t10.crop_id = t9.id
         inner join tektarim.city_crop_questions t11 on t11.city_crop_id = t10.id and t11.question_id = t4.id
where t1.answer <> t1.answer_new
  and t1.operation_status = 'approved'
  and t5.city_id between 2 and 82
order by t1.income_expense_answer_id desc, t1.udate desc
;

insert into tektarim.crop_coefficient_values (idate, crop_coefficient_id, value)
select t1.udate, t6.id, t1.data_value_new
from tvys.crop_coefficient_activity t1
         inner join tvys.crop_coefficient t2 on t1.crop_coefficient_id = t2.id
         inner join tvys.crop t3 on t1.crop_id = t3.id
         inner join tektarim.coefficients t4 on t2.data_type_key = t4.type and t2.data_type_value = t4.value_tr
         inner join tektarim.crops t5 on t3.name = t5.name
         inner join tektarim.crop_coefficients t6 on t5.id = t6.crop_id and t6.coefficient_id = t4.id
where t1.operation_status = 'approved'
  and t1.data_value <> t1.data_value_new
order by t6.id, t1.udate desc
;

INSERT INTO `tektarim`.`city_diesel_distance_values`
(`idate`,
 `city_diesel_distance_id`,
 `value`)
select cdda.udate, cdds.id as city_diesel_distance_id, cdda.transportation_coef_new
from tvys.city_diesel_distance_activity as cdda
         left join tvys.city as ci on cdda.city_id = ci.id
         left join tvys.cities as cis on ci.name = cis.name
         left join tektarim.city_diesel_distances as cdds on cdds.city_id = cis.id
where operation_status = 'approved'
  and cdds.type = 'TRANSPORTATION_COEFFICIENT'
  and transportation_coef_new is not null
  and cis.id is not null
order by cdda.id desc
;

insert into tektarim.general_coefficients (status, name)
SELECT 1, name
FROM tvys.general_coef
;

insert into tektarim.general_coefficient_values (idate, general_coefficient_id, value)
SELECT gca.idate, gcs.id, new_value
FROM tvys.general_coef_activity as gca
         left join tvys.general_coef as gc on gca.general_coef_id = gc.id
         left join tektarim.general_coefficients as gcs on gcs.name = gc.name
where gca.operation_status = 'approved'
order by gc.id, gca.udate desc
;

insert into tektarim.city_crop_seed_and_seedling_number_values (idate, city_crop_seed_and_seedling_number_id, value)
select t1.udate, tccssn.id, t1.hybrid_seedling_two_graft_new
from tvys.city_crop_seed_and_seedling_numbers_activity t1
         inner join tvys.city_crop_seed_and_seedling_numbers t2 on t2.id = t1.city_crop_seed_and_seedling_numbers_id
         inner join tvys.city_crop t3 on t3.id = t2.city_crop_id
         inner join tvys.city t4 on t4.id = t3.city_id
         inner join tektarim.cities tci on tci.name = t4.name
         inner join tvys.crop t5 on t5.id = t3.crop_id
         inner join tektarim.crops tcr on tcr.name = t5.name
         inner join tektarim.city_crops tcc on tcc.city_id = tci.id and tcc.crop_id = tcr.id
         inner join tektarim.city_crop_seed_and_seedling_numbers tccssn on tccssn.city_crop_id = tcc.id
where 1 = 1
  and t3.city_id between 2 and 82
  and t1.operation_status = 'approved'
  and t1.hybrid_seedling_two_graft <> t1.hybrid_seedling_two_graft_new
  and t1.hybrid_seedling_two_graft_new is not null
  and tccssn.type = 'HYBRID_SEEDLING_TWO_GRAFT'
order by tccssn.id, t1.udate desc
;

insert into tektarim.city_crop_seed_and_seedling_price_values (idate, city_crop_seed_and_seedling_price_id, value)
select t1.udate, tccssn.id, t1.`hybrid_seedling_two_graft_price_new`
from tvys.city_crop_seed_and_seedling_prices_activity t1
         inner join tvys.city_crop_seed_and_seedling_prices t2 on t2.id = t1.city_crop_seed_and_seedling_prices_id
         inner join tvys.city_crop t3 on t3.id = t2.city_crop_id
         inner join tvys.city t4 on t4.id = t3.city_id
         inner join tektarim.cities tci on tci.name = t4.name
         inner join tvys.crop t5 on t5.id = t3.crop_id
         inner join tektarim.crops tcr on tcr.name = t5.name
         inner join tektarim.city_crops tcc on tcc.city_id = tci.id and tcc.crop_id = tcr.id
         inner join tektarim.city_crop_seed_and_seedling_prices tccssn on tccssn.city_crop_id = tcc.id
where 1 = 1
  and t3.city_id between 2 and 82
  and t1.operation_status = 'approved'
  and t1.`hybrid_seedling_two_graft_price` <> t1.`hybrid_seedling_two_graft_price_new`
  and t1.`hybrid_seedling_two_graft_price_new` is not null
  and tccssn.type = 'HYBRID_SEEDLING_TWO_GRAFT'
order by tccssn.id, t1.udate desc
;

insert into tektarim.city_crop_field_harvest_average_values (idate, city_crop_id, collected_crop_count)
SELECT t1.idate, tcc.id, t1.collected_crop_count_new
FROM tvys.field_harvest_average_activity t1
         inner join tvys.city_crop t2 on t2.id = t1.city_crop_id
         inner join tvys.city t3 on t3.id = t2.city_id
         inner join tektarim.cities tci on tci.name = t3.name
         inner join tvys.crop t4 on t4.id = t2.crop_id
         inner join tektarim.crops tcr on tcr.name = t4.name
         inner join tektarim.city_crops tcc on tcc.city_id = tci.id and tcc.crop_id = tcr.id
where (t1.collected_crop_count <> t1.collected_crop_count_new or operation_type = 'create')
  and t2.city_id between 2 and 82
order by tcc.id, t1.idate desc
;

insert into tektarim.city_crop_watering_values (idate, city_crop_id, maintenance)
select t1.udate, tcc.id, t1.watering_maintenance_new
FROM tvys.city_watering_data_activity t1
         inner join tvys.city_watering_data t2 on t2.id = t1.city_watering_data_id
         inner join tvys.city_crop t3 on t3.id = t2.city_crop_id
         inner join tvys.city t4 on t4.id = t3.city_id
         inner join tektarim.cities tci on tci.name = t4.name
         inner join tvys.crop t5 on t5.id = t3.crop_id
         inner join tektarim.crops tcr on tcr.name = t5.name
         inner join tektarim.city_crops tcc on tcc.city_id = tci.id and tcc.crop_id = tcr.id
where t1.operation_status = 'approved'
  and t1.watering_maintenance <> t1.watering_maintenance_new
  and t3.city_id between 2 and 82
order by t2.id, t1.udate desc
;

insert into tektarim.city_fertilizer_values (idate, city_fertilizer_id, price)
SELECT t1.udate, tcfe.id, t1.fertilizer_price_new
FROM tvys.city_fertilizer_price_activity t1
         inner join tvys.city_fertilizer_price t2 on t2.id = t1.city_fertilizer_price_id
         inner join tvys.city t3 on t3.id = t2.city_id
         inner join tektarim.cities tci on tci.name = t3.name
         inner join tvys.fertilizer t4 on t4.id = t2.fertilizer_id
         inner join tektarim.fertilizers tfe on t4.id = tfe.old_fertilizer_id
         inner join tektarim.city_fertilizers tcfe on tcfe.city_id = tci.id and tcfe.fertilizer_id = tfe.id
where t1.fertilizer_price_new is not null
  and t1.fertilizer_price <> t1.fertilizer_price_new
  and t1.fertilizer_price_new is not null
  and operation_status = 'approved'
order by tcfe.id, t1.udate desc
;

insert into tektarim.city_fertilizer_values (idate, city_fertilizer_id, price)
select t1.udate, t5.id, t1.`foliar_fertilizer_cheap_new`
from tvys.city_foliar_fertilizers_activity t1
         inner join tvys.city_foliar_fertilizers t2 on t1.city_foliar_fertilizers_id = t2.id
         inner join tvys.city t3 on t2.city_id = t3.id
         inner join tektarim.cities t4 on t3.name = t4.name
         inner join tektarim.city_fertilizers t5 on t5.city_id = t4.id
where t1.operation_status = 'approved'
  and t1.`foliar_fertilizer_cheap_new` is not null
  and t1.`foliar_fertilizer_cheap` <> t1.`foliar_fertilizer_cheap_new`
  and t5.fertilizer_id = 33
order by t1.city_foliar_fertilizers_id, t1.udate desc
;

insert into tektarim.city_fertilizer_values (idate, city_fertilizer_id, price)
select t1.udate, t5.id, t1.`humic_acid_leonadid_cheap_new`
from tvys.city_soil_conditioners_activity t1
         inner join tvys.city_soil_conditioners t2 on t1.city_soil_conditioners_id = t2.id
         inner join tvys.city t3 on t2.city_id = t3.id
         inner join tektarim.cities t4 on t3.name = t4.name
         inner join tektarim.city_fertilizers t5 on t5.city_id = t4.id
where t1.operation_status = 'approved'
  and t1.`humic_acid_leonadid_cheap_new` is not null
  and t1.`humic_acid_leonadid_cheap` <> t1.`humic_acid_leonadid_cheap_new`
  and t5.fertilizer_id = 36
order by t1.city_soil_conditioners_id, t1.udate desc
;








