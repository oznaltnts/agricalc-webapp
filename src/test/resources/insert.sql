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
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER','İşçilik','OTHER_LABOR');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER','Mazot','OTHER_DIESEL');
INSERT INTO tektarim.coefficients (status, type, value_tr, value) values (1, 'OTHER','Malzeme','OTHER_MATERIAL');
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


