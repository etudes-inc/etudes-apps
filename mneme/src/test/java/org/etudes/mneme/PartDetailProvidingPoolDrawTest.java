/**********************************************************************************
 *
 * Copyright (c) 2017 Etudes, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.etudes.mneme;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.etudes.mneme.model.PartDetail;
import org.etudes.mneme.model.PartDetailProvidingPoolDraw;
import org.junit.Test;

public class PartDetailProvidingPoolDrawTest {

	class PartDetailConcrete extends PartDetail {

		protected int numQuestions = 0;
		protected float sumQuestionPoints = 0l;

		public PartDetailConcrete() {
			super();
		}

		public PartDetailConcrete(long id, Optional<Float> overrideSumQuestionPoints, int numQuestions, float sumQuestionPoints) {
			super(id, overrideSumQuestionPoints);
			this.numQuestions = numQuestions;
			this.sumQuestionPoints = sumQuestionPoints;
		}

		@Override
		public int getNumQuestions() {
			return this.numQuestions;
		}

		@Override
		public PartDetail set(PartDetail other) {
			super.set(other);

			if (!(other instanceof PartDetailConcrete)) {
				throw new IllegalArgumentException("PartDetailConcrete.set expects another PartDetailConcrete");
			}
			PartDetailConcrete o = (PartDetailConcrete) other;
			this.numQuestions = o.getNumQuestions();
			this.sumQuestionPoints = o.sumQuestionPoints();

			return this;
		}

		@Override
		public float sumQuestionPoints() {
			return this.sumQuestionPoints;
		}
	}

	protected final static long id = 22l;
	protected final static int numQuestions = 2;
	protected final static Optional<Float> overrideSumQuestionPoints = Optional.of(100f);
	protected final static long poolId = 42l;
	protected final static float secondOverridePoints = 300f;

	@Test
	public void test_getNumQuestions() {
		PartDetailProvidingPoolDraw d = new PartDetailProvidingPoolDraw(id, overrideSumQuestionPoints, numQuestions, poolId);
		Assertions.assertThat(d.getNumQuestions()).isEqualTo(numQuestions);
	}

	@Test
	public void test_overrideSumQuestionPoints_nullNotAllowed() {
		PartDetailProvidingPoolDraw d = new PartDetailProvidingPoolDraw(id, overrideSumQuestionPoints, numQuestions, poolId);
		Assertions.assertThat(d.getOverrideSumQuestionPoints()).isNotNull().isPresent();

		try {
			d.setOverrideSumQuestionPoints(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(d.getOverrideSumQuestionPoints()).isNotNull();

		d.setOverrideSumQuestionPoints(Optional.empty());
		Assertions.assertThat(d.getOverrideSumQuestionPoints()).isNotNull().isNotPresent();
	}

	@Test
	public void test_overrideSumQuestionPoints_overrides() {
		PartDetailProvidingPoolDraw d = new PartDetailProvidingPoolDraw(id, overrideSumQuestionPoints, numQuestions, poolId);
		Assertions.assertThat(d.getTotalPoints()).isEqualTo(overrideSumQuestionPoints.get());

		d.setOverrideSumQuestionPoints(Optional.empty());
		Assertions.assertThat(d.getTotalPoints()).isEqualTo(d.sumQuestionPoints());
	}

	@Test
	public void test_set() {
		PartDetailProvidingPoolDraw d = new PartDetailProvidingPoolDraw(id, overrideSumQuestionPoints, numQuestions, poolId);

		PartDetail d2 = new PartDetailProvidingPoolDraw().set(d);
		Assertions.assertThat(d2).isInstanceOf(PartDetailProvidingPoolDraw.class);
		Assertions.assertThat(d2).isNotNull();
		Assertions.assertThat(d).isEqualTo(d2);
	}

	@Test
	public void test_set_failsWithWrongType() {
		PartDetailProvidingPoolDraw d = new PartDetailProvidingPoolDraw(id, overrideSumQuestionPoints, numQuestions, poolId);

		try {
			new PartDetailConcrete().set(d);
			Assertions.fail("set with wrong type allowed");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void test_setEffectivePoints() {
		// the detail has an override to not match the natural points
		PartDetailProvidingPoolDraw d = new PartDetailProvidingPoolDraw(id, overrideSumQuestionPoints, numQuestions, poolId);
		Assertions.assertThat(d.getOverrideSumQuestionPoints()).isPresent();

		// setting to a new override should result in it still having an override
		d.setEffectivePoints(secondOverridePoints);
		Assertions.assertThat(d.getOverrideSumQuestionPoints()).isPresent().isEqualTo(Optional.of(Float.valueOf(secondOverridePoints)));

		// setting to the natural points value should clear the override
		d.setEffectivePoints(d.sumQuestionPoints());
		Assertions.assertThat(d.getOverrideSumQuestionPoints()).isNotPresent();
	}

	@Test
	public void test_sumQuestionPoints() {
		PartDetailProvidingPoolDraw d = new PartDetailProvidingPoolDraw(id, overrideSumQuestionPoints, numQuestions, poolId);
		Assertions.assertThat(d.sumQuestionPoints()).isEqualTo(0); // TODO:
	}
}
