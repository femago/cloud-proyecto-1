import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {Application} from './application.model';
import {BaseEntity, createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class ApplicationService {

    private resourceUrl =  SERVER_API_URL + 'api/applications';

    currentContest: BaseEntity = null;

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(application: Application): Observable<Application> {
        const copy = this.convert(application);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(application: Application): Observable<Application> {
        const copy = this.convert(application);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Application> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    queryPublicApplicationsByContest(contestId: number, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrl}/contests/${contestId}`, options)
            .map((res: Response) => this.convertResponse(res));
    }
    queryPrivateApplicationsByContest(contestId: number, req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrl}/contests/${contestId}/principal`, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Application.
     */
    private convertItemFromServer(json: any): Application {
        const entity: Application = Object.assign(new Application(), json);
        entity.createDate = this.dateUtils
            .convertDateTimeFromServer(json.createDate);
        return entity;
    }

    /**
     * Convert a Application to a JSON which can be sent to the server.
     */
    private convert(application: Application): Application {
        const copy: Application = Object.assign({}, application);

        copy.createDate = this.dateUtils.toDate(application.createDate);
        return copy;
    }

    downloadVoice(id: number) {

        return this._httpClient.get(`${this.resourceUrl}/${id}/voice/converted`, { responseType: 'blob'})
            .map(res => {
                return new Blob([res], { type: 'application/pdf', });
            });
    }
}
