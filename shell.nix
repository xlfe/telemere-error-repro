with (import <nixpkgs> { });
mkShell {
  buildInputs = [
    jdk21_headless
    (clojure.override { jdk = pkgs.jdk21_headless; })
  ];
}
