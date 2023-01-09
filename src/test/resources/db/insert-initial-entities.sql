INSERT INTO user_entity(id, name, email, gender, status)
VALUES
(1, 'Sweta Asan', 'asan_sweta@stanton-okuneva.com', 'female','active'),
(2, 'Sen. Drona Tagore', 'sen_tagore_drona@gutkowski.com', 'female','inactive'),
(3, 'Chandranath Mehra DVM', 'dvm_chandranath_mehra@weimann.com', 'male','inactive'),
(4255, 'Amb. Aditeya Deshpande', 'amb_aditeya_deshpande@dicki-kub.info', 'female','female');

INSERT INTO post_entity(id, user_id, data)
VALUES (1, 1, '{
  "body": "Umerus conventus demitto.",
  "title": "Tibi termes contra utique cerno apto auditor sortitus tandem fuga dolorem atrox coniecto."
}');


INSERT INTO file_entity (id, user_id, filename, type, size, data)
VALUES (1, 1, 'icon', 'png', 100, 'data');