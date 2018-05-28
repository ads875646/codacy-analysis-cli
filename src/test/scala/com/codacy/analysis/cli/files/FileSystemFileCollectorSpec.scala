package com.codacy.analysis.cli.files

import better.files.File
import cats.implicits._
import com.codacy.analysis.cli.clients.api._
import com.codacy.analysis.cli.configuration.{CodacyConfigurationFile, EngineConfiguration, LanguageConfiguration}
import com.codacy.analysis.cli.tools.ToolCollector
import com.codacy.api.dtos.Languages
import org.specs2.control.NoLanguageFeatures
import org.specs2.mutable.Specification

import scala.sys.process.Process
import scala.util.Success

class FileSystemFileCollectorSpec extends Specification with NoLanguageFeatures {

  val fsFc = new FileSystemFileCollector()

  val toolCollector = new ToolCollector(false)

  val expectedFiles = List(
    "src/main/resources/docs/directory-tests/rails3/config/initializers/inflections.rb",
    "src/main/resources/docs/description/Render.md",
    "src/main/resources/docs/directory-tests/rails4/app/helpers/application_helper.rb",
    "src/main/resources/docs/description/RenderDoS.md",
    "src/main/resources/docs/description/JRubyXML.md",
    "src/main/resources/docs/directory-tests/rails3/Gemfile",
    "src/main/resources/docs/description/BasicAuth.md",
    "src/main/resources/docs/directory-tests/rails3/config.ru",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/i18n.rb",
    "src/main/resources/docs/directory-tests/rails3/config/brakeman.yml",
    "src/main/resources/docs/directory-tests/rails3/app/models/account.rb",
    "src/main/resources/docs/directory-tests/rails3/script/rails",
    "README.md",
    "src/main/resources/docs/description/SafeBufferManipulation.md",
    "src/main/resources/docs/directory-tests/rails3/config/application.rb",
    "src/main/resources/docs/description/NestedAttributes.md",
    "src/main/resources/docs/directory-tests/rails3/config/environments/production.rb",
    "src/main/resources/docs/directory-tests/rails3/public/422.html",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/application_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/purchase.rb",
    "src/main/resources/docs/directory-tests/rails3/test/test_helper.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_params.html.erb",
    "src/main/resources/docs/directory-tests/rails3/config/environment.rb",
    "src/main/resources/docs/directory-tests/rails3/test/functional/home_controller_test.rb",
    "src/main/resources/docs/directory-tests/rails4/Gemfile",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/base_thing.rb",
    "src/main/resources/docs/directory-tests/rails3/public/images/rails.png",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/backtrace_silencers.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/products/index.html.erb",
    "src/main/resources/docs/directory-tests/rails4/app/views/users/eval_it.html.erb",
    "src/main/resources/docs/directory-tests/rails3/doc/README_FOR_APP",
    "src/main/resources/docs/description/UnscopedFind.md",
    "src/main/resources/docs/description/DigestDoS.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/index.html.erb",
    "src/main/resources/docs/directory-tests/rails3/public/javascripts/effects.js",
    "src/main/resources/docs/directory-tests/rails4/bin/rake",
    "src/main/resources/docs/directory-tests/rails4/app/views/users/index.html.erb",
    "project/build.properties",
    "src/main/resources/docs/directory-tests/rails4/config.ru",
    "src/main/resources/docs/patterns.json",
    "src/main/resources/docs/directory-tests/rails4/app/assets/images/rails.png",
    "src/main/resources/docs/description/CreateWith.md",
    "src/main/resources/docs/description/TranslateBug.md",
    "src/main/resources/docs/description/SelectVulnerability.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_model.html.erb",
    "src/main/resources/docs/description/SSLVerify.md",
    "src/main/scala/codacy/brakeman/Brakeman.scala",
    "src/main/resources/docs/directory-tests/rails4/config/application.rb",
    "circle.yml",
    "src/main/resources/docs/directory-tests/rails3/public/javascripts/rails.js",
    "src/main/resources/docs/directory-tests/rails3/lib/controller_filter.rb",
    "src/main/resources/docs/directory-tests/rails4/external_checks/check_external_check_test.rb",
    "src/main/resources/docs/description/ResponseSplitting.md",
    "src/main/resources/docs/description/SQLCVEs.md",
    "src/main/resources/docs/description/Deserialize.md",
    "src/main/resources/docs/directory-tests/rails4/public/favicon.ico",
    "src/main/resources/docs/directory-tests/rails3/public/500.html",
    "src/main/resources/docs/directory-tests/rails3/public/javascripts/application.js",
    "src/main/resources/docs/directory-tests/rails3/app/models/notifier.rb",
    "src/main/resources/docs/directory-tests/rails3/config/environments/development.rb",
    "src/main/resources/docs/directory-tests/rails4/public/404.html",
    "src/main/resources/docs/directory-tests/rails4/bin/bundle",
    "src/main/resources/docs/description/SimpleFormat.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_locals.html.erb",
    "src/main/resources/docs/description/CrossSiteScripting.md",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/nested_controller.rb",
    "src/main/resources/docs/description/JSONParsing.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_eval.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/views/layouts/application.html.erb",
    "src/main/resources/docs/directory-tests/rails4/test/test_helper.rb",
    "src/main/resources/docs/directory-tests/rails4/app/assets/stylesheets/application.css",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/_account.html.haml",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_mass_assignment.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/home_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/backtrace_silencers.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/before/use_filter12345.html.erb",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/mime_types.rb",
    "src/main/resources/docs/directory-tests/rails3/config/environments/test.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_render.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/views/child/action_in_child.html.erb",
    "src/main/resources/docs/directory-tests/rails3/config/routes.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/products/edit.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_content_tag.html.erb",
    "src/main/resources/docs/directory-tests/rails4/public/500.html",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/application_helper.rb",
    "src/main/resources/docs/directory-tests/rails3/test/functional/other_controller_test.rb",
    "src/main/resources/docs/description/LinkTo.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_collection.html.erb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/mime_types.rb",
    "src/main/resources/docs/directory-tests/rails4/config/boot.rb",
    "src/main/resources/docs/directory-tests/rails4/app/views/another/html_safe_is_not.html.erb",
    "src/main/resources/docs/description/Evaluation.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_redirect.html.erb",
    "src/main/resources/docs/directory-tests/rails3/public/index.html",
    "src/main/resources/docs/description/XMLDoS.md",
    "src/main/resources/docs/description/StripTags.md",
    "src/main/resources/docs/description/SanitizeMethods.md",
    "src/main/resources/docs/description/SessionSettings.md",
    "build.sbt",
    "src/main/resources/docs/directory-tests/rails3/public/favicon.ico",
    "LICENSE",
    "src/main/resources/docs/directory-tests/rails3/app/views/products/new.html.erb",
    "project/plugins.sbt",
    "src/main/resources/docs/directory-tests/rails3/db/seeds.rb",
    "src/main/resources/docs/description/LinkToHref.md",
    "src/main/resources/docs/directory-tests/rails4/app/assets/javascripts/application.js",
    "src/main/resources/docs/description/ContentTag.md",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/disable_xml_parsing.rb",
    "src/main/resources/docs/directory-tests/rails3/config/database.yml",
    "src/main/resources/docs/directory-tests/rails4/app/views/another/overflow.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/models/noticia.rb",
    "src/main/resources/docs/directory-tests/rails3/public/404.html",
    "src/main/resources/docs/directory-tests/rails3/app/views/products/_form.html.erb",
    "src/main/resources/docs/description/Send.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_filter.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/products_helper.rb",
    "src/main/resources/docs/directory-tests/rails4/app/views/another/various_xss.html.erb",
    "src/main/resources/docs/description/SymbolDoSCVE.md",
    "src/main/resources/docs/directory-tests/rails3/test/performance/browsing_test.rb",
    "src/main/resources/docs/directory-tests/rails4/app/models/account.rb",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/users_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/config/boot.rb",
    "src/main/resources/docs/directory-tests/rails4/app/models/recursive/stack_level.rb",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/friendly_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/config/locales/en.yml",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_command.html.erb",
    "src/main/resources/docs/directory-tests/rails4/config/environment.rb",
    "src/main/resources/docs/directory-tests/rails3/README",
    "src/main/resources/docs/directory-tests/rails4/app/models/user.rb",
    "src/main/resources/docs/directory-tests/rails4/app/views/another/use_params_in_regex.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/other_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/public/javascripts/dragdrop.js",
    "src/main/resources/docs/directory-tests/rails3/public/javascripts/prototype.js",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/before_controller.rb",
    "src/main/resources/docs/description/RenderInline.md",
    "src/main/resources/docs/description/JSONEncoding.md",
    "src/main/resources/docs/directory-tests/rails4/db/seeds.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/_user.html.erb",
    "src/main/resources/docs/directory-tests/rails4/app/views/users/haml_test.html.haml",
    "src/main/resources/docs/directory-tests/rails4/app/views/users/test_parse.html.erb",
    "src/main/resources/docs/description/ForgerySetting.md",
    "src/main/resources/docs/description/I18nXSS.md",
    "src/main/resources/docs/directory-tests/rails4/config/environments/development.rb",
    "src/main/resources/docs/description/FileAccess.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_select_tag.html.erb",
    "src/main/resources/docs/description/YAMLParsing.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/products/show.html.erb",
    "src/main/resources/docs/directory-tests/rails3/test/unit/helpers/other_helper_test.rb",
    "src/main/resources/docs/description/ModelAttributes.md",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/home_helper.rb",
    "src/main/resources/docs/directory-tests/rails4/config/brakeman.ignore",
    "src/main/resources/docs/description/ModelAttrAccessible.md",
    "src/main/resources/docs/directory-tests/rails4/Rakefile",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/products_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/config/locales/en.yml",
    "src/main/resources/docs/description/DefaultRoutes.md",
    "src/main/resources/docs/description/WithoutProtection.md",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/secret_token.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/product.rb",
    "src/main/resources/docs/tool-description.md",
    "src/main/resources/docs/directory-tests/rails4/config/environments/test.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/filter_parameter_logging.rb",
    "src/main/resources/docs/directory-tests/rails3/Gemfile.lock",
    "src/main/resources/docs/description/UnsafeReflection.md",
    "src/main/resources/docs/description/FileDisclosure.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/before/use_filters12.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/models/underline_model.rb",
    "src/main/resources/docs/description/EscapeFunction.md",
    "src/main/resources/docs/directory-tests/rails4/app/models/email.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_mail_to.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_file_access.html.erb",
    "src/main/scala/codacy/Engine.scala",
    "src/main/resources/docs/description/QuoteTableName.md",
    "src/main/resources/docs/directory-tests/rails4/public/robots.txt",
    "src/main/resources/docs/description/HeaderDoS.md",
    "src/main/resources/docs/description/SelectTag.md",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/inflections.rb",
    "src/main/resources/docs/description/Execute.md",
    "src/main/resources/docs/directory-tests/rails4/app/views/_global_partial.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/child_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_send_file.html.erb",
    "src/main/resources/docs/directory-tests/rails4/lib/tasks/some_task.rb",
    "src/main/resources/docs/directory-tests/rails4/README.rdoc",
    "src/main/resources/docs/directory-tests/rails4/config/database.yml",
    "src/main/resources/docs/directory-tests/rails3/public/javascripts/controls.js",
    "src/main/resources/docs/directory-tests/rails4/lib/sweet_lib.rb",
    "src/main/resources/docs/directory-tests/rails3/public/robots.txt",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/another_controller.rb",
    "src/main/resources/docs/description/SkipBeforeFilter.md",
    "src/main/resources/docs/description/DetailedExceptions.md",
    "src/main/resources/docs/directory-tests/rails4/app/api/api.rb",
    "src/main/resources/docs/description/MassAssignment.md",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_iteration.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_object.html.erb",
    "src/main/resources/docs/directory-tests/rails4/bin/rails",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_cookie.html.erb",
    "src/main/resources/docs/description/SingleQuotes.md",
    "src/main/resources/docs/directory-tests/rails3/app/models/bill.rb",
    "src/main/resources/docs/description/Redirect.md",
    "src/main/resources/docs/directory-tests/rails4/config/environments/production.rb",
    "src/main/resources/docs/directory-tests/rails4/config/routes.rb",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_dynamic_render.html.erb",
    "src/main/resources/docs/description/SQL.md",
    "src/main/resources/docs/directory-tests/rails4/public/422.html",
    "src/main/resources/docs/description/description.json",
    "src/main/resources/docs/description/MailTo.md",
    "src/main/resources/docs/description/RegexDoS.md",
    "src/main/resources/docs/description/ValidationRegex.md",
    "src/main/resources/docs/directory-tests/rails3/test/unit/helpers/home_helper_test.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/application_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/app/views/layouts/application.html.erb",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/session_store.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/user.rb",
    "src/main/resources/docs/directory-tests/rails3/Rakefile",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/other_helper.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/wrap_parameters.rb",
    "src/main/resources/docs/description/SymbolDoS.md",
    "src/main/resources/docs/description/FilterSkipping.md",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/session_store.rb",
    "src/main/resources/docs/description/ModelSerialize.md",
    "src/main/resources/docs/directory-tests/rails4/test/mailers/.keep",
    "src/main/resources/docs/directory-tests/rails3/public/stylesheets/.gitkeep",
    "src/main/resources/docs/directory-tests/rails3/lib/tasks/.gitkeep",
    "src/main/resources/docs/directory-tests/rails4/lib/tasks/.keep",
    "src/main/resources/docs/directory-tests/rails4/log/.keep",
    "src/main/resources/docs/directory-tests/rails4/vendor/assets/javascripts/.keep",
    "src/main/resources/docs/directory-tests/rails4/test/helpers/.keep",
    "src/main/resources/docs/directory-tests/rails4/app/mailers/.keep",
    "src/main/resources/docs/directory-tests/rails4/vendor/assets/stylesheets/.keep",
    "src/main/resources/docs/directory-tests/rails4/app/models/concerns/.keep",
    ".gitignore",
    "src/main/resources/docs/directory-tests/rails4/test/models/.keep",
    "src/main/resources/docs/directory-tests/rails4/.gitignore",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/concerns/.keep",
    "src/main/resources/docs/directory-tests/rails4/app/models/.keep",
    "src/main/resources/docs/directory-tests/rails4/lib/assets/.keep",
    "src/main/resources/docs/directory-tests/rails4/test/integration/.keep",
    "src/main/resources/docs/directory-tests/rails3/vendor/plugins/.gitkeep",
    "src/main/resources/docs/directory-tests/rails4/test/controllers/.keep",
    "src/main/resources/docs/directory-tests/rails4/test/fixtures/.keep",
    "src/main/resources/docs/directory-tests/rails3/.gitignore",
    "src/main/resources/docs/directory-tests/rails3/app/views/home/test_sql.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/views/other/test_strip_tags.html.erb",
    "src/main/resources/docs/directory-tests/rails3/app/views/whatever/wherever/nested/so_nested.html.erb")

  val expectedToolFiles = List(
    "src/main/resources/docs/directory-tests/rails3/config/initializers/inflections.rb",
    "src/main/resources/docs/directory-tests/rails4/app/helpers/application_helper.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/i18n.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/account.rb",
    "src/main/resources/docs/directory-tests/rails3/config/application.rb",
    "src/main/resources/docs/directory-tests/rails3/config/environments/production.rb",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/application_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/purchase.rb",
    "src/main/resources/docs/directory-tests/rails3/test/test_helper.rb",
    "src/main/resources/docs/directory-tests/rails3/config/environment.rb",
    "src/main/resources/docs/directory-tests/rails3/test/functional/home_controller_test.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/base_thing.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/backtrace_silencers.rb",
    "src/main/resources/docs/directory-tests/rails4/config/application.rb",
    "src/main/resources/docs/directory-tests/rails3/lib/controller_filter.rb",
    "src/main/resources/docs/directory-tests/rails4/external_checks/check_external_check_test.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/notifier.rb",
    "src/main/resources/docs/directory-tests/rails3/config/environments/development.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/nested_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/test/test_helper.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/home_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/backtrace_silencers.rb",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/mime_types.rb",
    "src/main/resources/docs/directory-tests/rails3/config/environments/test.rb",
    "src/main/resources/docs/directory-tests/rails3/config/routes.rb",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/application_helper.rb",
    "src/main/resources/docs/directory-tests/rails3/test/functional/other_controller_test.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/mime_types.rb",
    "src/main/resources/docs/directory-tests/rails4/config/boot.rb",
    "src/main/resources/docs/directory-tests/rails3/db/seeds.rb",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/disable_xml_parsing.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/noticia.rb",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/products_helper.rb",
    "src/main/resources/docs/directory-tests/rails3/test/performance/browsing_test.rb",
    "src/main/resources/docs/directory-tests/rails4/app/models/account.rb",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/users_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/config/boot.rb",
    "src/main/resources/docs/directory-tests/rails4/app/models/recursive/stack_level.rb",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/friendly_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/config/environment.rb",
    "src/main/resources/docs/directory-tests/rails4/app/models/user.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/other_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/before_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/db/seeds.rb",
    "src/main/resources/docs/directory-tests/rails4/config/environments/development.rb",
    "src/main/resources/docs/directory-tests/rails3/test/unit/helpers/other_helper_test.rb",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/home_helper.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/products_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/secret_token.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/product.rb",
    "src/main/resources/docs/directory-tests/rails4/config/environments/test.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/filter_parameter_logging.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/underline_model.rb",
    "src/main/resources/docs/directory-tests/rails4/app/models/email.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/inflections.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/child_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/lib/tasks/some_task.rb",
    "src/main/resources/docs/directory-tests/rails4/lib/sweet_lib.rb",
    "src/main/resources/docs/directory-tests/rails4/app/controllers/another_controller.rb",
    "src/main/resources/docs/directory-tests/rails4/app/api/api.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/bill.rb",
    "src/main/resources/docs/directory-tests/rails4/config/environments/production.rb",
    "src/main/resources/docs/directory-tests/rails4/config/routes.rb",
    "src/main/resources/docs/directory-tests/rails3/test/unit/helpers/home_helper_test.rb",
    "src/main/resources/docs/directory-tests/rails3/app/controllers/application_controller.rb",
    "src/main/resources/docs/directory-tests/rails3/config/initializers/session_store.rb",
    "src/main/resources/docs/directory-tests/rails3/app/models/user.rb",
    "src/main/resources/docs/directory-tests/rails3/app/helpers/other_helper.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/wrap_parameters.rb",
    "src/main/resources/docs/directory-tests/rails4/config/initializers/session_store.rb")

  val expectedConfigFiles = List("src/main/resources/docs/directory-tests/rails3/config/brakeman.yml")

  "FileSystemFileCollector" should {
    "list files and filter files per tool" in {
      (for {
        directory <- File.temporaryDirectory()
      } yield {

        Process(Seq("git", "clone", "git://github.com/qamine-test/codacy-brakeman", directory.pathAsString)).!
        Process(Seq("git", "reset", "--hard", "b10790d724e5fd2ca98e8ba3711b6cb10d7f5e38"), directory.toJava).!

        val tool = toolCollector.from("brakeman").right.get

        val result = for {
          filesTargetGlobal <- fsFc.list(
            directory,
            "Local configuration not found".asLeft,
            "Remote configuration not found".asLeft)
          filesTargetTool <- fsFc.filter(
            tool,
            filesTargetGlobal,
            "Local configuration not found".asLeft,
            "Remote configuration not found".asLeft)
        } yield (filesTargetGlobal, filesTargetTool)

        result must beSuccessfulTry

        result must beLike {
          case Success((filesTargetGlobal, filesTargetTool)) =>
            filesTargetGlobal.directory must be(directory)
            filesTargetGlobal.files.to[List].map(_.toString) must containTheSameElementsAs(expectedFiles)
            fsFc.hasConfigurationFiles(tool, filesTargetGlobal) must beTrue

            filesTargetTool.directory must be(directory)
            filesTargetTool.files.map(_.toString) must containTheSameElementsAs(expectedToolFiles)
            fsFc.hasConfigurationFiles(tool, filesTargetTool) must beFalse
        }
      }).get()
    }

    "list files and filter files per tool with remote excludes" in {
      (for {
        directory <- File.temporaryDirectory()
      } yield {

        val expectedToolFiles = List(
          "src/main/scala/codacy/Engine.scala",
          "src/main/scala/codacy/brakeman/Brakeman.scala",
          "src/main/scala/codacy/Test1.scala",
          "src/main/scala/codacy/TestWeird.sc")

        val remoteConfiguration = ProjectConfiguration(
          Set(FilePath("src/main/scala/codacy/brakeman/Test2.scala")),
          Some(Set.empty),
          Set(LanguageExtensions(Languages.Scala, Set(".sc"))),
          Set()).asRight

        Process(Seq("git", "clone", "git://github.com/qamine-test/codacy-brakeman", directory.pathAsString)).!
        Process(Seq("git", "reset", "--hard", "32f7302bcd4f1afbfb94b7365e20120120943a10"), directory.toJava).!

        val tool = toolCollector.from("scalastyle").right.get

        val result = for {
          filesTargetGlobal <- fsFc.list(directory, "Local configuration not found".asLeft, remoteConfiguration)
          filesTargetTool <- fsFc.filter(
            tool,
            filesTargetGlobal,
            "Local configuration not found".asLeft,
            remoteConfiguration)
        } yield filesTargetTool

        result must beSuccessfulTry

        result must beLike {
          case Success(filesTargetTool) =>
            filesTargetTool.directory must be(directory)
            filesTargetTool.files.map(_.toString) must containTheSameElementsAs(expectedToolFiles)
            fsFc.hasConfigurationFiles(tool, filesTargetTool) must beFalse
        }
      }).get()
    }

    "list files and filter files per tool with local excludes" in {
      (for {
        directory <- File.temporaryDirectory()
      } yield {

        val expectedToolFiles = List(
          "src/main/scala/codacy/Engine.scala",
          "src/main/scala/codacy/brakeman/Brakeman.scala",
          "src/main/scala/codacy/TestWeird.sc")

        val localConfiguration = CodacyConfigurationFile(
          Option(Map("scalastyle" -> EngineConfiguration(Some(Set(Glob("**/brakeman/Test2.scala"))), None, None))),
          Option(Set(Glob("**/Test1.scala"))),
          Option(Map((Languages.Scala, LanguageConfiguration(Option(Set(".sc"))))))).asRight

        Process(Seq("git", "clone", "git://github.com/qamine-test/codacy-brakeman", directory.pathAsString)).!
        Process(Seq("git", "reset", "--hard", "32f7302bcd4f1afbfb94b7365e20120120943a10"), directory.toJava).!

        val tool = toolCollector.from("scalastyle").right.get

        val result = for {
          filesTargetGlobal <- fsFc.list(directory, localConfiguration, "Remote configuration not found".asLeft)
          filesTargetTool <- fsFc.filter(
            tool,
            filesTargetGlobal,
            localConfiguration,
            "Remote configuration not found".asLeft)
        } yield filesTargetTool

        result must beSuccessfulTry

        result must beLike {
          case Success(filesTargetTool) =>
            filesTargetTool.directory must be(directory)
            filesTargetTool.files.map(_.toString) must containTheSameElementsAs(expectedToolFiles)
            fsFc.hasConfigurationFiles(tool, filesTargetTool) must beFalse
        }
      }).get()
    }

    "list files and filter files per tool with default ignores" in {
      (for {
        directory <- File.temporaryDirectory()
      } yield {

        val expectedToolFiles = List(
          "src/main/scala/codacy/Engine.scala",
          "src/main/scala/codacy/Test1.scala",
          "src/main/scala/codacy/TestWeird.sc")

        val remoteConfiguration = ProjectConfiguration(
          Set.empty,
          Some(Set(PathRegex(""".*/main/scala/codacy/brakeman/.*"""))),
          Set(LanguageExtensions(Languages.Scala, Set(".sc"))),
          Set()).asRight

        Process(Seq("git", "clone", "git://github.com/qamine-test/codacy-brakeman", directory.pathAsString)).!
        Process(Seq("git", "reset", "--hard", "32f7302bcd4f1afbfb94b7365e20120120943a10"), directory.toJava).!

        val tool = Tool.from("scalastyle").right.get

        val result = for {
          filesTargetGlobal <- fsFc.list(directory, "Local configuration not found".asLeft, remoteConfiguration)
          filesTargetTool <- fsFc.filter(
            tool,
            filesTargetGlobal,
            "Local configuration not found".asLeft,
            remoteConfiguration)
        } yield filesTargetTool

        result must beSuccessfulTry

        result must beLike {
          case Success(filesTargetTool) =>
            filesTargetTool.directory must be(directory)
            filesTargetTool.files.map(_.toString) must containTheSameElementsAs(expectedToolFiles)
            fsFc.hasConfigurationFiles(tool, filesTargetTool) must beFalse
        }
      }).get()
    }

    "list files and filter files per tool with local excludes and remote ignores (should ignore remote ignores in favor of local configuration)" in {
      (for {
        directory <- File.temporaryDirectory()
      } yield {

        val expectedToolFiles = List(
          "src/main/scala/codacy/Engine.scala",
          "src/main/scala/codacy/brakeman/Brakeman.scala",
          "src/main/scala/codacy/TestWeird.sc")

        val localConfiguration = CodacyConfigurationFile(
          Option(Map("scalastyle" -> EngineConfiguration(Some(Set(Glob("**/brakeman/Test2.scala"))), None, None))),
          Option(Set(Glob("**/Test1.scala"))),
          Option(Map((Languages.Scala, LanguageConfiguration(Option(Set(".sc"))))))).asRight
        val remoteConfiguration = ProjectConfiguration(
          Set.empty,
          Some(Set(PathRegex(""".*/main/scala/codacy/brakeman/.*"""))),
          Set(LanguageExtensions(Languages.Scala, Set(".sc"))),
          Set()).asRight

        Process(Seq("git", "clone", "git://github.com/qamine-test/codacy-brakeman", directory.pathAsString)).!
        Process(Seq("git", "reset", "--hard", "32f7302bcd4f1afbfb94b7365e20120120943a10"), directory.toJava).!

        val tool = Tool.from("scalastyle").right.get

        val result = for {
          filesTargetGlobal <- fsFc.list(directory, localConfiguration, remoteConfiguration)
          filesTargetTool <- fsFc.filter(
            tool,
            filesTargetGlobal,
            localConfiguration,
            "Remote configuration not found".asLeft)
        } yield filesTargetTool

        result must beSuccessfulTry

        result must beLike {
          case Success(filesTargetTool) =>
            filesTargetTool.directory must be(directory)
            filesTargetTool.files.map(_.toString) must containTheSameElementsAs(expectedToolFiles)
            fsFc.hasConfigurationFiles(tool, filesTargetTool) must beFalse
        }
      }).get()
    }
  }

}
