package com.lukaspaczos.emergencynumber.communication.network.api.numbers;

import java.util.List;

/**
 * Created by lukas on 14.11.17
 */

public class EmergencyNumbersPOJO {

    /**
     * disclaimer : The data from this API is provided without any claims of accuracy, you should use this data as guidance, and do your own due diligence.
     * error :
     * data : {"country":{"name":"Poland","ISOCode":"PL","ISONumeric":"616"},"ambulance":{"all":["999"],"gsm":null,"fixed":null},"fire":{"all":["998"],"gsm":null,"fixed":null},"police":{"all":["997"],"gsm":null,"fixed":null},"dispatch":{"all":[""],"gsm":null,"fixed":null},"member_112":true,"localOnly":false,"nodata":false}
     */

    private String disclaimer;
    private String error;
    private DataBean data;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * country : {"name":"Poland","ISOCode":"PL","ISONumeric":"616"}
         * ambulance : {"all":["999"],"gsm":null,"fixed":null}
         * fire : {"all":["998"],"gsm":null,"fixed":null}
         * police : {"all":["997"],"gsm":null,"fixed":null}
         * dispatch : {"all":[""],"gsm":null,"fixed":null}
         * member_112 : true
         * localOnly : false
         * nodata : false
         */

        private CountryBean country;
        private AmbulanceBean ambulance;
        private FireBean fire;
        private PoliceBean police;
        private DispatchBean dispatch;
        private boolean member_112;
        private boolean localOnly;
        private boolean nodata;

        public CountryBean getCountry() {
            return country;
        }

        public void setCountry(CountryBean country) {
            this.country = country;
        }

        public AmbulanceBean getAmbulance() {
            return ambulance;
        }

        public void setAmbulance(AmbulanceBean ambulance) {
            this.ambulance = ambulance;
        }

        public FireBean getFire() {
            return fire;
        }

        public void setFire(FireBean fire) {
            this.fire = fire;
        }

        public PoliceBean getPolice() {
            return police;
        }

        public void setPolice(PoliceBean police) {
            this.police = police;
        }

        public DispatchBean getDispatch() {
            return dispatch;
        }

        public void setDispatch(DispatchBean dispatch) {
            this.dispatch = dispatch;
        }

        public boolean isMember_112() {
            return member_112;
        }

        public void setMember_112(boolean member_112) {
            this.member_112 = member_112;
        }

        public boolean isLocalOnly() {
            return localOnly;
        }

        public void setLocalOnly(boolean localOnly) {
            this.localOnly = localOnly;
        }

        public boolean isNodata() {
            return nodata;
        }

        public void setNodata(boolean nodata) {
            this.nodata = nodata;
        }

        public static class CountryBean {
            /**
             * name : Poland
             * ISOCode : PL
             * ISONumeric : 616
             */

            private String name;
            private String ISOCode;
            private String ISONumeric;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getISOCode() {
                return ISOCode;
            }

            public void setISOCode(String ISOCode) {
                this.ISOCode = ISOCode;
            }

            public String getISONumeric() {
                return ISONumeric;
            }

            public void setISONumeric(String ISONumeric) {
                this.ISONumeric = ISONumeric;
            }
        }

        public static class AmbulanceBean {
            /**
             * all : ["999"]
             * gsm : null
             * fixed : null
             */

            private List<String> gsm;
            private List<String> fixed;
            private List<String> all;

            public List<String> getGsm() {
                return gsm;
            }

            public void setGsm(List<String> gsm) {
                this.gsm = gsm;
            }

            public List<String> getFixed() {
                return fixed;
            }

            public void setFixed(List<String> fixed) {
                this.fixed = fixed;
            }

            public List<String> getAll() {
                return all;
            }

            public void setAll(List<String> all) {
                this.all = all;
            }
        }

        public static class FireBean {
            /**
             * all : ["998"]
             * gsm : null
             * fixed : null
             */

            private List<String> gsm;
            private List<String> fixed;
            private List<String> all;

            public List<String> getGsm() {
                return gsm;
            }

            public void setGsm(List<String> gsm) {
                this.gsm = gsm;
            }

            public List<String> getFixed() {
                return fixed;
            }

            public void setFixed(List<String> fixed) {
                this.fixed = fixed;
            }

            public List<String> getAll() {
                return all;
            }

            public void setAll(List<String> all) {
                this.all = all;
            }
        }

        public static class PoliceBean {
            /**
             * all : ["997"]
             * gsm : null
             * fixed : null
             */

            private List<String> gsm;
            private List<String> fixed;
            private List<String> all;

            public List<String> getGsm() {
                return gsm;
            }

            public void setGsm(List<String> gsm) {
                this.gsm = gsm;
            }

            public List<String> getFixed() {
                return fixed;
            }

            public void setFixed(List<String> fixed) {
                this.fixed = fixed;
            }

            public List<String> getAll() {
                return all;
            }

            public void setAll(List<String> all) {
                this.all = all;
            }
        }

        public static class DispatchBean {
            /**
             * all : [""]
             * gsm : null
             * fixed : null
             */

            private List<String> gsm;
            private List<String> fixed;
            private List<String> all;

            public List<String> getGsm() {
                return gsm;
            }

            public void setGsm(List<String> gsm) {
                this.gsm = gsm;
            }

            public List<String> getFixed() {
                return fixed;
            }

            public void setFixed(List<String> fixed) {
                this.fixed = fixed;
            }

            public List<String> getAll() {
                return all;
            }

            public void setAll(List<String> all) {
                this.all = all;
            }
        }
    }
}
