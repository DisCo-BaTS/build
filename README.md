
<!-- HEADER START -->
<h1 align="center" style="margin: 0; line-height: 1.25em">
  <img src="https://github.com/DisCo-BaTS/.github/blob/main/profile/assets/logo/discobats_logo_square_icon.png?raw=true"/>
  <p style="margin: 0">
    DisCo-BaTS
  </p>
</h1>
<h2 align="center" style="margin: 0; line-height: 0.25em">
  <p style="margin-top: 0; padding-bottom: 8px">
    <em>Dis</em>tributed <em>Com</em>ponent-<em>Ba</em>sed <em>T</em>raffic <em>S</em>imulation
  </p>   
</h2>

<p align="center" style="margin-bottom: 0">
  <a href="https://opensource.org/license/lgpl-3-0">
    <img alt="License" src="https://img.shields.io/badge/license-lgpl--3.0-success?style=for-the-badge"/>
  </a>
  <a href="https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html">
    <img alt="Build Status" src="https://img.shields.io/badge/Java%20version-21-F58219?logo=java&style=for-the-badge"/>
  </a>
</p>
<p align="center" style="margin: 0">
  <a href="https://www.docker.com/" style="line-height: 0.25em">
    <img alt="Build Status" src="https://img.shields.io/badge/docker-257bd6?style=for-the-badge&logo=docker&logoColor=white"/>
  </a>
  <a href="https://maven.apache.org/">
    <img alt="API" src="https://img.shields.io/badge/Apache%20Maven-003063?style=for-the-badge&logo=Apache%20Maven&logoColor=white.svg"/>
  </a>
  <a href="https://www.jetbrains.com/idea/">
    <img alt="Build Status" src="https://img.shields.io/badge/Intellij%20Idea-000?logo=intellij-idea&style=for-the-badge&color=5F2395"/>
  </a>
</p>

<p align="center">
  <em>
    <p align="center" style="margin-bottom: 0">
        A framework for highly flexible scenario modeling and direct simulation execution <br/>
        without the need for manual model-transformations or adjustments of the simulation application.
    </p>
    <p align="center" style="margin-top: 0.5em">
        Centered around a unified meta-model that explicitly targets scenario-based simulation testing of<br/>
        various software-based systems and system-components, which can be located both locally and remotely.
    </p>
</em>
</p>
<!-- HEADER END -->

---

## Project Structure
- __CORE__
    - [**meta**](https://github.com/DisCo-BaTS/meta)
        - [metamodel](https://github.com/DisCo-BaTS/meta/tree/main/metamodel)
        - [annotation](https://github.com/DisCo-BaTS/meta/tree/main/annotation)
    - [**application**](https://github.com/DisCo-BaTS/application)
        - [core](https://github.com/DisCo-BaTS/application/tree/main/core)
        - [root](https://github.com/DisCo-BaTS/application/tree/main/root)
        - [remote](https://github.com/DisCo-BaTS/application/tree/main/remote)
        - [router](https://github.com/DisCo-BaTS/application/tree/main/router)
    - [**build**](https://github.com/DisCo-BaTS/build)
        - [_plugins_](https://github.com/DisCo-BaTS/build/tree/main/plugins)
            - [_maven-plugins_](https://github.com/DisCo-BaTS/build/tree/main/plugins/maven-plugins)
                - [mvn-jaxb-index-builder](https://github.com/DisCo-BaTS/build/tree/main/plugins/maven-plugins/mvn-jaxb-index-builder)

- __EXAMPLES__
    - [**models**](https://github.com/DisCo-BaTS/models)
    - [**testunits**](https://github.com/DisCo-BaTS/testunits)


- __TOOLS__
    - [**webview**](https://github.com/DisCo-BaTS/webview)
    - [**editor**](https://github.com/DisCo-BaTS/editor)


- __ADDITIONAL__
    - [**templates**](https://github.com/DisCo-BaTS/templates)
    - [**assets**](https://github.com/DisCo-BaTS/assets) (configs, scenarios, misc)


---

> [!IMPORTANT]  
> This framework was developed as part of a doctoral thesis submitted on February 19, 2026, titled
> _"Flexible Komponentenbasierte Modellierung und Simulation von Szenarien für die Verifizierung und Validierung
> automatisierter maritimer Fahrsysteme"_ (eng.: _"Flexible component-based modeling and simulation of scenarios for the verification and validation of automated
> maritime navigation systems."_).  
> **Although a lot of work has been put into it, be aware that the software is still in a proof-of-concept stage** and you may find currently unused code that was once used to explore alternative solutions, undocumented parts, missing comments, and other imperfections.

---

## Documentation
For documentation check out the wiki pages and the READMEs located in the individual repositories.
> Additionally, once the dissertation in which DisCo-BaTS was developed has been successfully defended and published,
> the URL to the corresponding publicly available PDF version will be added here.


## Attribution
If you use the DisCo-BaTS modeling and simulation framework or parts of it for your own research,
it would be appreciated if you would include the following reference in all published work for which
DisCo-BaTS or parts of it where used:
> A citable reference will be added here once the corresponding dissertation has been successfully defended and published.


## Related Repositories

[OpenLVC / poRTIco](https://github.com/openlvc/portico) is utilized as the implementation of the Runtime Infrastructure (RTI)
according to the High Level Architecture (HLA) standard for distributed cooperative simulation coupling in the
version of 2010 ([IEEE 1516:2010](https://standards.ieee.org/ieee/1516/3744/)). The version of poRTIco used is `2.1.3`.  
PoRTIco is awesome - go support the maintainers! 💜


## Contact

Any questions regarding DisCo-BaTS can be asked, discussed, and found in the [discussion section](https://github.com/orgs/DisCo-BaTS/discussions).


## License

Distributed Component-Based Traffic Simulation (DisCo-BaTS)  
Copyright (C) 2026 David Reiher <https://github.com/dvdrhr>

This program is free software: you can redistribute it and/or modify it under the terms of the
GNU Lesser General Public License version 3 as published by the Free Software Foundation

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU Lesser General Public License version 3 for more details.

You should have received a copy of the GNU Lesser General Public License along with this program.  
If not, see <https://www.gnu.org/licenses/lgpl+gpl-3.0.txt> and <https://www.gnu.org/licenses/lgpl-3.0.en.html>.