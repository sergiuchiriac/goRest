insert into user_entity(id, name, email, gender, status)
values
(1, 'Sweta Asan', 'asan_sweta@stanton-okuneva.com', 'female','active'),
(2, 'Sen. Drona Tagore', 'sen_tagore_drona@gutkowski.com', 'female','inactive'),
(3, 'Chandranath Mehra DVM', 'dvm_chandranath_mehra@weimann.com', 'male','inactive'),
(4255, 'Amb. Aditeya Deshpande', 'amb_aditeya_deshpande@dicki-kub.info', 'female','female');

insert into post_entity(id, user_id, data)
values (1, 1, '{
  "body": "Umerus conventus demitto.",
  "title": "Tibi termes contra utique cerno apto auditor sortitus tandem fuga dolorem atrox coniecto."
}');


insert into file_entity (id, user_id, filename, type, size, data)
values (1, 1, 'icon', 'png', 100, 'data');

insert into token_entity(id, token) values (1, 'Bearer 3206f8330b48b01774f5d4d73b3bac0ac1531ad85452cec1d7df1a106e979b55');