INSERT INTO cities (code, name, neighbors_ids)
VALUES ('01', 'Adana', '33,80,46,38,51,31'),
       ('02', 'Adıyaman', '27,63,21,44,46'),
       ('03', 'Afyonkarahisar', '26,42,32,15,20,64,43'),
       ('04', 'Ağrı', '76,36,25,49,13,65'),
       ('05', 'Amasya', '55,60,66,19'),
       ('06', 'Ankara', '71,18,14,26,42,68,40'),
       ('07', 'Antalya', '33,70,42,32,15,48'),
       ('08', 'Artvin', '53,25,75'),
       ('09', 'Aydın', '35,45,20,48'),
       ('10', 'Balıkesir', '17,35,45,43,16'),
       ('11', 'Bilecik', '16,54,14,26,43'),
       ('12', 'Bingöl', '24,25,49,21,23,62'),
       ('13', 'Bitlis', '49,4,65,56,72'),
       ('14', 'Bolu', '81,54,11,26,6,18,67,78'),
       ('15', 'Burdur', '32,7,48,20,3'),
       ('16', 'Bursa', '77,41,54,11,43,10'),
       ('17', 'Çanakkale', '10,59'),
       ('18', 'Çankırı', '78,37,19,71,6,14'),
       ('19', 'Çorum', '57,55,5,66,71,18,37'),
       ('20', 'Denizli', '45,64,3,15,48,9'),
       ('21', 'Diyarbakır', '63,2,44,23,12,49,72,47'),
       ('22', 'Edirne', '39,59'),
       ('23', 'Elazığ', '44,24,62,12,21'),
       ('24', 'Erzincan', '58,28,29,69,25,12,62,23,44'),
       ('25', 'Erzurum', '8,75,36,4,49,12,24,69,53'),
       ('26', 'Eskişehir', '11,14,6,42,3,43'),
       ('27', 'Gaziantep', '79,63,2,46,80,31'),
       ('28', 'Giresun', '61,29,24,58,52'),
       ('29', 'Gümüşhane', '61,69,24,28'),
       ('30', 'Hakkari', '65,73'),
       ('31', 'Hatay', '1,80,27'),
       ('32', 'Isparta', '3,42,7,15'),
       ('33', 'Mersin', '7,70,42,51,1'),
       ('34', 'İstanbul', '41,59'),
       ('35', 'İzmir', '10,45,9'),
       ('36', 'Kars', '75,76,4,25'),
       ('37', 'Kastamonu', '74,78,18,19,57'),
       ('38', 'Kayseri', '58,46,1,51,50,66'),
       ('39', 'Kırklareli', '22,59,34'),
       ('40', 'Kırşehir', '6,71,66,50,68'),
       ('41', 'Kocaeli', '34,77,16,54'),
       ('42', 'Konya', '6,26,3,32,7,70,33,51,68'),
       ('43', 'Kütahya', '16,11,26,3,64,45,10'),
       ('44', 'Malatya', '23,21,2,46,58,24'),
       ('45', 'Manisa', '35,9,20,64,43,10'),
       ('46', 'Kahramanmaraş', '58,44,2,27,80,1,38'),
       ('47', 'Mardin', '73,56,72,21,63'),
       ('48', 'Muğla', '7,15,20,9'),
       ('49', 'Muş', '12,25,4,13,72,21'),
       ('50', 'Nevşehir', '66,38,51,68,40'),
       ('51', 'Niğde', '50,38,1,33,42,68'),
       ('52', 'Ordu', '55,60,58,28'),
       ('53', 'Rize', '61,69,25,8'),
       ('54', 'Sakarya', '41,16,11,14,81'),
       ('55', 'Samsun', '57,19,5,60,52'),
       ('56', 'Siirt', '13,65,73,47,72'),
       ('57', 'Sinop', '37,19,55'),
       ('58', 'Sivas', '66,38,46,44,24,28,52,60'),
       ('59', 'Tekirdağ', '34,39,22,17'),
       ('60', 'Tokat', '5,55,52,58,66'),
       ('61', 'Trabzon', '28,29,69,53'),
       ('62', 'Tunceli', '24,12,23'),
       ('63', 'Şanlıurfa', '27,2,21,47'),
       ('64', 'Uşak', '45,43,3,20'),
       ('65', 'Van', '30,73,56,13,4'),
       ('66', 'Yozgat', '19,5,60,58,38,50,40,71'),
       ('67', 'Zonguldak', '74,78,14,81'),
       ('68', 'Aksaray', '42,6,40,50,51'),
       ('69', 'Bayburt', '29,61,53,25,24'),
       ('70', 'Karaman', '42,33,7'),
       ('71', 'Kırıkkale', '6,18,19,66,40'),
       ('72', 'Batman', '21,49,13,56,47'),
       ('73', 'Şırnak', '47,56,65,30'),
       ('74', 'Bartın', '67,78,37'),
       ('75', 'Ardahan', '8,25,36'),
       ('76', 'Iğdır', '36,4'),
       ('77', 'Yalova', '34,41,16'),
       ('78', 'Karabük', '67,74,37,18,14'),
       ('79', 'Kilis', '27'),
       ('80', 'Osmaniye', '1,31,27,46'),
       ('81', 'Düzce', '54,14,67')
;

insert into city_crops (`status`, `city_id`, `crop_id`)
select 1,tci.id, tcr.id
from tektarim.cities tci
cross join tektarim.crops tcr
order by tci.id, tcr.id
;

insert into city_crop_questions (`status`, `city_crop_id`, `question_id`)
select 1, cc.id, q.id
from city_crops cc
cross join questions q
order by cc.id, q.id
;

INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'NEEDED_DIESEL','Bir sürümde sıra arası gerekli mazot / derin sürüm ort. ("L/da")','NEEDED_DIESEL_DEEP');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'NEEDED_DIESEL','Bir sürümde sıra üzeri gerekli mazot / ikincil işlemler ort. ("L/da")','NEEDED_DIESEL_SECOND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WATERING','Damla – insan','WATERING_DROP_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WATERING','Damla - makine','WATERING_DROP_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WATERING','Yağmur - insan','WATERING_RAIN_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WATERING','Yağmur - makine','WATERING_RAIN_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WATERING','Vahşi - insan','WATERING_WILD_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WATERING','Vahşi - makine','WATERING_WILD_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','Ekim dikim – insan','FERTILIZER_PLANTING_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','Ekim dikim – makine','FERTILIZER_PLANTING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','Makine ile – insan','FERTILIZER_MACHINE_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','Makine ile – makine','FERTILIZER_MACHINE_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','El ile – insan','FERTILIZER_HAND_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','El ile – makine','FERTILIZER_HAND_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','Sulama sistemi ile – insan','FERTILIZER_WATERING_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'FERTILIZER','Sulama sistemi ile – makine','FERTILIZER_WATERING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','Sırt makinesi – insan','PEST_CONTROL_BACK_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','Sırt makinesi – makine','PEST_CONTROL_BACK_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','Traktör – insan','PEST_CONTROL_TRACTOR_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','Traktör – makine','PEST_CONTROL_TRACTOR_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','El ile – insan','FERTILIZER_HAND_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','El ile – makine','FERTILIZER_HAND_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','Sulama sistemi ile – insan','FERTILIZER_WATERING_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'PEST_CONTROL','Sulama sistemi ile – makine','FERTILIZER_WATERING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WEEDING','El ile çapa ("sıra üzeri") – insan','WEEDING_HAND_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WEEDING','El ile çapa ("sıra üzeri") – makine','WEEDING_HAND_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WEEDING','El çapa makinesi ("sıra üzeri") – insan','WEEDING_MACHINE_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'WEEDING','El çapa makinesi ("sıra üzeri") – makine','WEEDING_MACHINE_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'DURATION','Bir kişinin 1 dekar budama süresi – insan','DURATION_PRUNING_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'DURATION','Bir kişinin 1 dekar budama süresi – makine','DURATION_PRUNING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'DURATION','Bir kişinin 1 dekar meyve seyreltme süresi – insan','DURATION_THINNING_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'DURATION','Bir kişinin 1 dekar meyve seyreltme süresi – makine','DURATION_THINNING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'DURATION','Bir kişinin yazlık yaprak alma, uç alma veya yazlık budama süresi – insan','DURATION_SUMMER_PRUNING_HUMAN');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'DURATION','Bir kişinin yazlık yaprak alma, uç alma veya yazlık budama süresi – makine','DURATION_SUMMER_PRUNING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_COEFFICIENT','İşçilik','OTHER_LABOR');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_COEFFICIENT','Mazot','OTHER_DIESEL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_COEFFICIENT','Malzeme','OTHER_MATERIAL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEEDLING','El ("adet")','HOW_MUCH_SEEDLING_HAND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEEDLING','Makine ("lt")','HOW_MUCH_SEEDLING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEEDLING','El ve makine – El','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEEDLING','El ve makine – Makine','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEED','El ("adet")','HOW_MUCH_SEEDLING_HAND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEED','Makine ("lt")','HOW_MUCH_SEEDLING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEED','El ve makine – El','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_SEED','El ve makine – Makine','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_COLLECT','Makine ("lt")','HOW_MUCH_SEEDLING_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_COLLECT','El ve makine – El','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HOW_MUCH_COLLECT','El ve makine – Makine','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'CANAL','İşçilik','OTHER_LABOR');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'CANAL','Mazot','OTHER_DIESEL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'SEEDLING_PREPARATION','İşçilik','OTHER_LABOR');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'SEEDLING_PREPARATION','Mazot','OTHER_DIESEL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'MULCH','İşçilik','OTHER_LABOR');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'MULCH','Mazot','OTHER_DIESEL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'MULCH','Miktar','MULCH_AMOUNT');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'BLEND','El','BLEND_HAND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'BLEND','Makine','BLEND_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'BLEND','El ve makine – El','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'BLEND','El ve makine – Makine','HOW_MUCH_SEEDLING_HAND_AND_MACHINE_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HAYMAKER','İşçilik','OTHER_LABOR');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HAYMAKER','Makine','BLEND_MACHINE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'HAYMAKER','Makinenin saatlik işleme kapasitesi ("kg")','HAYMAKER_MACHINE_CAPACITY');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'BALE','İşçilik','OTHER_LABOR');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'BALE','Mazot','OTHER_DIESEL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'BALE','Balya kapasitesi','BALE_CAPACITY');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_CONSTANTS','Traktör römorkunun taşıma kapasitesi','OTHER_CONSTANTS_TRACTOR_TRAILER');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_CONSTANTS','Araç taşıma kapasitesi','OTHER_CONSTANTS_VEHICLE_CAPACITY');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_CONSTANTS','Bir kişi ne kadar ürün işler, kurutur','OTHER_CONSTANTS_ONE_PERSON_HANDLE');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_CONSTANTS','Bir birim işleme malzemesinin işleyebildiği miktar','OTHER_CONSTANTS_HANDLE_MATERIAL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER_CONSTANTS','Bir kişi ne kadar ürün ambalajlar','OTHER_CONSTANTS_ONE_PERSON_PACKAGING');

INSERT INTO tektarim.crop_coefficients (status, crop_id, coefficient_id)
select 1, crops.id, coefficients.id
from tektarim.coefficients
cross join tektarim.crops
;

insert into tektarim.city_diesel_distances (status, city_id, type)
SELECT 1, city_id, type FROM agricalc.city_diesel_distances
;

insert into tektarim.city_crop_seed_and_seedling_numbers (status, city_crop_id, type)
select 1, id, 'LOCAL_SEED_KG' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEED_KG' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEEDLING' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEEDLING_ONE_GRAFT' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEEDLING_TWO_GRAFT' from tektarim.city_crops
order by id
;

insert into tektarim.city_crop_seed_and_seedling_prices (status, city_crop_id, type)
select 1, id, 'LOCAL_SEED_KG' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEED_KG' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEEDLING' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEEDLING_ONE_GRAFT' from tektarim.city_crops
union all
select 1, id, 'HYBRID_SEEDLING_TWO_GRAFT' from tektarim.city_crops
order by id
;

INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (1,'2026-04-20 10:44:59','2026-05-04 13:38:26',1,'COMPOUND_FERTILIZERS','10-15-20 20SO3 Zn',10.000,15.000,20.000,18);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (2,'2026-04-20 10:44:59','2026-05-04 13:38:57',1,'COMPOUND_FERTILIZERS','10-25-5 5CaO',10.000,25.000,5.000,19);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (3,'2026-04-20 10:44:59','2026-05-04 13:39:27',1,'COMPOUND_FERTILIZERS','12-12-17 (+2MgO+2OSO3+ME) (Akıllı)',12.000,12.000,17.000,28);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (4,'2026-04-20 10:44:59','2026-05-04 13:39:40',1,'COMPOUND_FERTILIZERS','13-24-12+10(SO3)+Zn+Fe (Mısır Gübresi)',13.000,24.000,12.000,27);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (5,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','13-24-12 4S FE (Mısır Gübresi)',13.000,24.000,12.000,20);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (6,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','14-7-17 (+2MgO+2OSO3+ME) (Akıllı)',14.000,7.000,17.000,29);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (7,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','15-15-15',15.000,15.000,15.000,12);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (8,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','15-15-15 Zn',15.000,15.000,15.000,13);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (9,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','15-15-15 Zn2OS',15.000,15.000,15.000,14);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (10,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','20-20-0',20.000,20.000,0.000,16);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (11,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','20-20-0 Zn',20.000,20.000,0.000,17);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (12,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','20-20-20',20.000,20.000,20.000,15);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (13,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','20-20-30+30(SO3)+Zn (Buğday Gübresi)',20.000,20.000,30.000,32);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (14,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','23-12-9 Zn',23.000,12.000,9.000,21);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (15,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','25-5-0',25.000,5.000,0.000,22);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (16,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','25-5-10-25 (Çay Gübresi)',25.000,5.000,10.000,23);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (17,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','Di Amonyum Fosfat (DAP)',18.000,46.000,0.000,11);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (18,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','Potasyum Nitrat',13.000,0.000,45.500,9);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (19,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','Süper Çotanak (Fındık Gübresi)',18.000,14.000,15.000,25);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (20,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'COMPOUND_FERTILIZERS','Süper Pancar-S (Şekerpancarı Gübresi)',13.000,16.000,15.000,24);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (21,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'NITROGEN_FERTILIZERS','Amonyum Nitrat',33.000,0.000,0.000,1);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (22,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'NITROGEN_FERTILIZERS','Amonyum Sülfat (Şeker Gübresi)',21.000,0.000,0.000,5);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (23,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'NITROGEN_FERTILIZERS','Kalsiyum Amonyum Nitrat (CAN)',13.000,0.000,0.000,2);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (24,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'NITROGEN_FERTILIZERS','Kalsiyum Nitrat',15.500,0.000,0.000,3);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (25,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'NITROGEN_FERTILIZERS','ÜRE',46.000,0.000,0.000,4);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (26,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'PHOSPHOR_FERTILIZERS','Mono Amonyum Fosfat (MAP)',11.000,52.000,0.000,10);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (27,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'PHOSPHOR_FERTILIZERS','Normal Süper Fosfat',0.000,16.500,0.000,6);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (28,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'PHOSPHOR_FERTILIZERS','Triple Süper Fosfat (TSP)',0.000,42.000,0.000,7);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (29,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'POTASSIUM_FERTILIZERS','Potasyum Nitrat',13.000,0.000,46.000,30);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (30,'2026-04-20 10:44:59','2026-05-04 13:43:38',1,'POTASSIUM_FERTILIZERS','Potasyum Sülfat',0.000,0.000,51.000,8);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (31,'2026-04-29 11:13:43',NULL,1,'FOLIAR_FERTILIZERS','Yaprak gübresi orta',NULL,NULL,NULL,NULL);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (32,'2026-04-29 11:13:43',NULL,1,'FOLIAR_FERTILIZERS','Yaprak gübresi pahalı',NULL,NULL,NULL,NULL);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (33,'2026-04-29 11:13:43',NULL,1,'FOLIAR_FERTILIZERS','Yaprak gübresi ucuz',NULL,NULL,NULL,NULL);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (34,'2026-04-29 11:13:43',NULL,1,'SOIL_CONDITIONERS','Humik asit, leonadid orta',NULL,NULL,NULL,NULL);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (35,'2026-04-29 11:13:43',NULL,1,'SOIL_CONDITIONERS','Humik asit, leonadid pahalı',NULL,NULL,NULL,NULL);
INSERT INTO tektarim.fertilizers (`id`,`idate`,`udate`,`status`,`type`,`name`,`nitrogen_percent`,`phosphor_percent`,`potassium_percent`,`old_fertilizer_id`) VALUES (36,'2026-04-29 11:13:43',NULL,1,'SOIL_CONDITIONERS','Humik asit, leonadid ucuz',NULL,NULL,NULL,NULL);

INSERT INTO `tektarim`.`users` (`status`, `phone`, `password`) VALUES (1, '+905348387707', '85d8333e6a0a459a4013ab471d8c239f5c12cc78ce046c5462ed11baa3301868');

INSERT INTO `tektarim`.`user_roles` (`user_id`, `role`) VALUES (1, 1);

INSERT INTO `tektarim`.`user_preferences` (`user_id`, `menu_mode`, `dark_mode`, `component_theme`, `topbar_theme`, `menu_theme`, `input_style`, `light_logo`)
VALUES (1, 'layout-horizontal', 'dark', 'green', 'dark', 'dark', 'outlined', 1);

INSERT INTO `tektarim`.`dairy_cow_coefficients` (`cow_type`, `value`) VALUES ('0', '1');
INSERT INTO `tektarim`.`dairy_cow_coefficients` (`cow_type`, `value`) VALUES ('1', '0.85');
INSERT INTO `tektarim`.`dairy_cow_coefficients` (`cow_type`, `value`) VALUES ('2', '0.6');
INSERT INTO `tektarim`.`dairy_cow_coefficients` (`cow_type`, `value`) VALUES ('3', '0.4');
INSERT INTO `tektarim`.`dairy_cow_coefficients` (`cow_type`, `value`) VALUES ('4', '0.25');

INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Yonca (Kuru)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Çayır Otu / Mera Otu (Kuru)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Saman (Arpa, Buğday, Çavdar, Çeltik)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Fiğ Otu (Kuru)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Korunga (Kuru)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Yulaf Otu');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Çavdar Otu');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Buğday Otu');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Arpa Otu');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,0,'Ayrık Otu');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Mısır Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Yonca Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Sorgum Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Sudan Otu Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Fiğ Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Buğday Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Arpa Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Tritikale Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,1,'Ayçiçeği Silajı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Taze Yonca');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Taze Fiğ');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Taze Çayır Otları');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Yeşil Mısır');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Yeşil Arpa');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Yeşil Buğday');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Yeşil Sorgum');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Yeşil Tritikale');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Pancar Yaprağı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,2,'Diğer Lifli Kaba Yemler (Mısır, Ayçiçeği, Baklagil Sapları)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,0,10,'Diğer Kaba Yemler');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Arpa (Dane)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Buğday (Dane)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Mısır (Dane)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Yulaf (Dane)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Tritikale (Dane)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Buğday Kepeği');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Razmol');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Melas');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Mısır Gluteni');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,3,'Hayvan Pancarı');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,4,'Soya Küspesi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,4,'Ayçiçeği Küspesi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,4,'Kanola Küspesi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,4,'Pamuk Tohumu Küspesi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,4,'DDGS (Damıtık Kurutulmuş Tahıl ve Çözünürleri)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,4,'Balık Unu');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,1,10,'Diğer Kesif Yemler');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,5,'Süt Yemi (%14 Protein)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,5,'Süt Yemi (%16 Protein)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,5,'Süt Yemi (%18 Protein)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,5,'Süt Yemi (%19 Protein)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,5,'Süt Yemi (%20 Protein)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,5,'Süt Yemi (%21 Protein)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,6,'Besi Başlangıç Yemi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,6,'Buzağı Başlangıç yemi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,6,'Besi Büyütme Yemi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,6,'Dana büyütme yemi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,6,'Besi Sonu Yemi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,6,'Besi Yemi (%19 Protein)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,10,'Sanayi yemi');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,2,10,'Diğer Karma Yemler');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,3,7,'Yeşil Taze Otlar');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,3,7,'Havuç');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,3,7,'Patates');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,3,7,'Mısır Hasıl');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,3,7,'Pancar Posası (Yaş)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,3,10,'Diğer Sulu Yemler');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,4,8,'Kireç Taşı (Kalsiyum Kaynağı)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,4,8,'DCP (Dikalsiyum Fosfat)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,4,8,'Tuz (Sodyum Klorür / NaCl)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,4,8,'Mineral Bloklar / Yalama Taşları');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,4,9,'Premiks (Vitamin-Mineral Karışımı)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,4,10,'Diğer Mineral ve Vitamin');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,5,7,'Probiyotikler');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,5,7,'Maya (Rumen Düzenleyici)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,5,7,'Enzimler');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,5,7,'Toksin Bağlayıcılar');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,5,7,'Bikarbonat (Sodyum Bikarbonat - Rumen Tamponu)');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,5,10,'Diğer Fonksiyonel Katkılar');
INSERT INTO `feeds` (`status`, `category`, `feed_type`, `name`) VALUES (1,6,10,'Diğer');

INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,0,'Çitlikte Maaşlı Çalışan Bakım Personeli');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,1,'Maaş almadan çalışan Aile İşgücü (küçük aile  işletmeler)');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,2,'Aylık Benzin Tüketimi (lt)');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,2,'Aylık Mazot Tüketimi (lt)');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,2,'Aylık Su Tüketimi (ton)');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,2,'Aylık Eletrik Tüketimi (Kwa)');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,3,'Aylık Veteriner gideri Ortalaması (TL) (Düzenli maaş hariç)');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,4,'Ortalama Tohulama Sayısı');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,5,'Sigorta Yaptırdınız mı?');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,6,'İşletmenin Kullandığı Sübvansiyonlu Kredi');
INSERT INTO `dairy_cow_costs` (`status`, `cost_type`, `name`) VALUES (1,7,'İşletmenin Kullandığı Sübvansiyonsuz Kredi');

INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'İşletmenin hayvan başına elde ettiği süt verimi ', '(Lt/Gün/1 İnek)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'İşletmenin çiğ süt net satış fiyatı (Kalite Primi Dahil)', '(TL/Lt)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Ortalama reforme inek fiyatı', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Ortalama gebe düve satış fiyatı', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Ortalama düve fiyatı', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Ortalama dana fiyatı', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Ortalama buzağı fiyatı', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Ortalama inek fiyatı', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Süt destek primi', '(TL/Lt)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Buzağı desteği', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Soy kütüğü desteği', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Hastalıktan ari işletme desteği', '(TL/Adet)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Gübre satış yada biyogaz feliriniz varsa giriniz', '(TL/Yıl)');
INSERT INTO `dairy_cow_incomes` (`status`, `name`, `unit`) VALUES ('1', 'Varsa diğer gelir giriniz', '(TL/Yıl)');

##plantation
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (1,1,'Acur');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (2,1,'Adaçayı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (3,1,'Ahududu');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (4,1,'Alabaş');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (5,1,'Altınçilek');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (6,1,'Anason');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (7,1,'Antepfıstığı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (8,1,'Armut');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (9,1,'Arpa (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (10,1,'Arpa (Silaj)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (11,1,'Arpa (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (12,1,'Arpa (Yemlik)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (13,1,'Aspir (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (14,1,'Aspir (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (15,1,'Avakado');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (16,1,'Ayçiçeği (Çerezlik)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (17,1,'Ayçiçeği (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (18,1,'Ayçiçeği (Yağlık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (19,1,'Ayva');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (20,1,'Badem');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (21,1,'Bakla (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (22,1,'Bakla (Taze)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (23,1,'Bakla (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (24,1,'Balkabağı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (25,1,'Bamya');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (26,1,'Bamya (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (27,1,'Barbunya (Fasulye) (Kuru) (Yer)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (28,1,'Barbunya (Fasulye) (Taze) (Sırık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (29,1,'Barbunya (Fasulye) (Taze) (Yer)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (30,1,'Barbunya (Fasulye) (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (31,1,'Bezelye (Taze) (Sırık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (32,1,'Bezelye (Taze) (Yer)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (33,1,'Bezelye (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (34,1,'Bezelye (Yer) (Dane) (Sanayilik)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (35,1,'Biber');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (36,1,'Böğürtlen');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (37,1,'Börülce (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (38,1,'Börülce (Taze)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (39,1,'Brokoli');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (40,1,'Buğday (Ekmeklik)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (41,1,'Buğday (Makarnalık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (42,1,'Buğday (Silaj)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (43,1,'Buğday (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (44,1,'Burçak (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (45,1,'Burçak (Ot)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (46,1,'Caramba İtalyan Çimi (Ot)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (47,1,'Caramba İtalyan Çimi (Silaj)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (48,1,'Ceviz');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (49,1,'Çam Fıstığı (Kozalak)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (50,1,'Çavdar (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (51,1,'Çavdar (Ot)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (52,1,'Çavdar (Silaj)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (53,1,'Çay');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (54,1,'Çayırotu (Doğal Biçim)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (55,1,'Çayırotu (Üretim)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (56,1,'Çeltik (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (57,1,'Çeltik (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (58,1,'Çilek');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (59,1,'Çim (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (60,1,'Çörekotu');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (61,1,'Defne');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (62,1,'Dereotu');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (63,1,'Domater (Kurutmalık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (64,1,'Domater (Salçalık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (65,1,'Domater (Sırık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (66,1,'Domater (Tarla)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (67,1,'Dut (Kurutmalık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (68,1,'Dut (Taze)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (69,1,'Ejder Meyvesi');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (70,1,'Elma');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (71,1,'Enginar');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (72,1,'Erik');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (73,1,'Fasülye (Kuru) (Yer)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (74,1,'Fasülye (Taze) (Sırık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (75,1,'Fasülye (Taze) (Yer)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (76,1,'Fasülye (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (77,1,'Fesleğen');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (78,1,'Fındık');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (79,1,'Fiğ');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (80,1,'Fiğ (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (81,1,'Frenk Üzümü');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (82,1,'Frezya');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (83,1,'Gerbera');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (84,1,'Goji Bery (Kurt Üzümü)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (85,1,'Greyfurt');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (86,1,'Guava');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (87,1,'Gül (Kesme)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (88,1,'Gül (Yağlık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (89,1,'Haşhaş (Kapsül)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (90,1,'Havuç');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (91,1,'Hayvan Pancarı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (92,1,'Hıyar');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (93,1,'Hünnap');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (94,1,'Ispanak');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (95,1,'İncir (Kuru)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (96,1,'İncir (Taze)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (97,1,'Kabak (Sakız)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (98,1,'Kabak Çekirdeği');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (99,1,'Kamkat');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (100,1,'Karanfil');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (101,1,'Karnabahar');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (102,1,'Karpuz');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (103,1,'Kavun');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (104,1,'Kayısı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (105,1,'Kayısı (Kurutmalık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (106,1,'Kaymak Ağacı (Fequa)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (107,1,'Keçiboynuzu');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (108,1,'Kekik');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (109,1,'Kereviz (Kök)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (110,1,'Kenevir (Lif)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (111,1,'Kereviz (Sap)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (112,1,'Kenevir (Tıbbi)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (113,1,'Kenevir (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (114,1,'Kestane');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (115,1,'Keten (Lif)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (116,1,'Keten (Yağlık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (117,1,'Kızılcık');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (118,1,'Kimyon');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (119,1,'Kinoa');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (120,1,'Kiraz');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (121,1,'Kişniş');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (122,1,'Kivi');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (123,1,'Kolza (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (124,1,'Kolza (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (125,1,'Kornit Üzümü');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (126,1,'Korunga (Ot)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (127,1,'Korunga (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (128,1,'Krizantem');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (129,1,'Kudret Narı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (130,1,'Kuş Üzümü');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (131,1,'Kuş Yemi');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (132,1,'Kuşkonmaz');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (133,1,'Lahana (Beyaz)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (134,1,'Lahana (Brüksel)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (135,1,'Lahana (Kara)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (136,1,'Lahana (Kırmızı)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (137,1,'Lale (Soğan)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (138,1,'Lavanta');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (139,1,'Lilyum');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (140,1,'Limon');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (141,1,'Mandalina');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (142,1,'Marul');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (143,1,'Maş Fasulyesi');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (144,1,'Maviyemiş (Yaban Mersini)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (145,1,'Maydanoz');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (146,1,'Mercimek');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (147,1,'Mısır (Cin)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (148,1,'Mısır (Çerezlik)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (149,1,'Mısır (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (150,1,'Mısır (Hasıl)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (151,1,'Mısır (Silaj)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (152,1,'Mısır (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (153,1,'Muşmula');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (154,1,'Muz');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (155,1,'Mürdümük (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (156,1,'Mürdümük (Ot)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (157,1,'Nane (Kurutmalık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (158,1,'Nane (Taze)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (159,1,'Nar');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (160,1,'Nektarin');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (161,1,'Nohut');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (162,1,'Pamuk');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (163,1,'Patates');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (164,1,'Patates (Sözleşmeli)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (165,1,'Patlıcan');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (166,1,'Pazı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (167,1,'Pepino');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (168,1,'Pırasa');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (169,1,'Pikan Cevizi');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (170,1,'Pomelo (Şadok)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (171,1,'Portakal');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (172,1,'Reygrass (Süt Otu)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (173,1,'Reyhan');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (174,1,'Rezene');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (175,1,'Roka');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (176,1,'Rozmanin (Biberiye)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (177,1,'Safran');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (178,1,'Sahlep');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (179,1,'Sarımsak (Kuru)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (180,1,'Sarımsak (Taze)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (181,1,'Semizotu');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (182,1,'Soğan (Arpacık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (183,1,'Soğan (Baş)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (184,1,'Soğan (Taze)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (185,1,'Soğan (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (186,1,'Sorgum (Sudan Otu) (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (187,1,'Sorgum (Sudan Otu) (Silaj)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (188,1,'Sorgum (Sudan Otu) (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (189,1,'Soya');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (190,1,'Soya (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (191,1,'Stevia (Şekerotu)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (192,1,'Sumak');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (193,1,'Susam');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (194,1,'Susam (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (195,1,'Süpürge Otu');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (196,1,'Şalgam');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (197,1,'Şeftali');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (198,1,'Şekerpancarı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (199,1,'Şekerpancarı (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (200,1,'Şerbetçiotu');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (201,1,'Şevketi Bostan');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (202,1,'Tere');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (203,1,'Trabzon Hurması');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (204,1,'Tritikale (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (205,1,'Tritikale (Silaj)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (206,1,'Turp');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (207,1,'Tütün');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (208,1,'Üzüm (Kurutmalık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (209,1,'Üzüm (Sofralık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (210,1,'Üzüm (Şaraplık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (211,1,'Vişne');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (212,1,'Yem Bezelyesi');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (213,1,'Yem Şalgamı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (214,1,'Yenidünya');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (215,1,'Yer Elması');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (216,1,'Yerfıstığı');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (217,1,'Yonca');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (218,1,'Yonca (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (219,1,'Yulaf (Dane)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (220,1,'Yulaf (Ot)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (221,1,'Yulaf (Tohumluk)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (222,1,'Zeytin (Sofralık)');
INSERT INTO `plantation_products` (`id`,`status`,`name`) VALUES (223,1,'Zeytin (Yağlık)');

