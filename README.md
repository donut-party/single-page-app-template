# party.donut/single-page-app

Template for a Donut single page app. Uses the minimal project as its base.

## Usage

This is a template project for use with [deps-new](https://github.com/seancorfield/deps-new).
As originally generated, it will produce a new library project when run:


``` bash
clojure \
  -Sdeps '{:deps {party.donut/single-page-app {:local/root "./"}}}'\
  -Tnew create \
  :template party.donut/single-page-app \
  :name group-name/app-name \
  :target-dir ../your-target-directory
```

Assuming you have installed `deps-new` as your `new` "tool" via:

```bash
clojure -Ttools install-latest :lib io.github.seancorfield/deps-new :as new
```

> Note: once the template has been published (to a public git repo), the invocation will be the same, except the `:local/root` dependency will be replaced by a git or Maven-like coordinate.

Run this template project's tests (by default, this just validates your template's `template.edn`
file -- that it is valid EDN and it satisfies the `deps-new` Spec for template files):

``` bash
clojure -T:build test
```

## Development

To update this template, we copy over files from `minimal` and perform some
transformations to get the files template-ready.

This is done in a shell script, as is the command to try the template:

``` bash
./bin/run update
./bin/run try # ensure the template works
```
